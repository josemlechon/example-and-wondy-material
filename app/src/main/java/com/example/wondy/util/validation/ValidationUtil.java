/*
 * Copyright (C) 2015 The Android Open Source Project
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
 *
 */

package com.example.wondy.util.validation;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.Iterator;
import java.util.List;

/**
 * @author jose m lechon on 01/02/2016
 * @version 1.0.0
 * @since 1
 */
public class ValidationUtil {

    public static final String PATTERN_SPANISH_PHONE = "(\\+?0{0,2}(?:34)?(?:9|6|7)\\d{8})";

    public static void validate(Validation... args) {
        for (Validation validation : args) {
            validation.validate();
        }
    }

    public static void pruneInvalid(List<? extends Validation> validations) {
        Iterator<? extends Validation> itr = validations.iterator();
        while (itr.hasNext()) {
            try {
                Validation next = itr.next();
                next.validate();
            } catch (ValidationFailedException ex) {
                itr.remove();
            }
        }
    }


    public static boolean validatePhone(String phone) {

        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).find();

    }

    public static boolean validateEmail(String email) {

        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static boolean isStringValid(String inString) {
        return inString != null && !inString.equals("") && inString.length() > 0 && !inString.trim().equals("null");
    }
}
