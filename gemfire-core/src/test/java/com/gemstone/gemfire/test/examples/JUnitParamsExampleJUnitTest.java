/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gemstone.gemfire.test.examples;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.gemstone.gemfire.test.junit.categories.UnitTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@Category(UnitTest.class)
@RunWith(JUnitParamsRunner.class)
public class JUnitParamsExampleJUnitTest {
  @Test
  @Parameters({"17, false", 
               "22, true" })
  public void personIsAdult(int age, boolean valid) throws Exception {
    assertThat(true, is(true));
    assertThat(new Person(age).isAdult(), is(valid));
  }
  
  protected static class Person {
    private static final int MIN_AGE_OF_ADULT = 18;
    private final int age;
    public Person(final int age) {
      this.age = age;
    }
    public Boolean isAdult() {
      return this.age >= MIN_AGE_OF_ADULT;
    }
  }
}
