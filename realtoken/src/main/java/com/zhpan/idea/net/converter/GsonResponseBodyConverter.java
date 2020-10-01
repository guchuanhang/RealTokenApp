/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhpan.idea.net.converter;

import com.google.gson.TypeAdapter;
import com.zhpan.idea.net.common.ErrorCode;
import com.zhpan.idea.net.exception.NoDataExceptionException;
import com.zhpan.idea.net.exception.ServerResponseException;
import com.zhpan.idea.net.exception.TokenInvalidException;
import com.zhpan.idea.net.exception.TokenNotExistException;
import com.zhpan.idea.net.module.BasicResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, Object> {

    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;

    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        try {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(getReaderContent(value.charStream()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            int status = jsonObject.optInt("status");
            switch (status) {
                case ErrorCode.SUCCESS: {
                    if (jsonObject.has("data")) {
                        BasicResponse rsp = (BasicResponse) adapter.fromJson(jsonObject.toString());
                        return rsp.getData();
                    } else {
                        throw new NoDataExceptionException();
                    }
                }
                case ErrorCode.TOKEN_EXPIRE:
                    throw new TokenInvalidException();

                case ErrorCode.REFRESH_TOKEN_EXPIRE:
                    throw new TokenNotExistException();
                default:
                    throw new ServerResponseException(status, jsonObject.optString("msg"));
            }
        } finally {
            value.close();
        }
    }

    private String getReaderContent(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        StringBuffer sb = new StringBuffer();
        String rowStr;
        while (null != (rowStr = br.readLine())) {
            sb.append(rowStr);
        }
        return sb.toString();
    }
}
