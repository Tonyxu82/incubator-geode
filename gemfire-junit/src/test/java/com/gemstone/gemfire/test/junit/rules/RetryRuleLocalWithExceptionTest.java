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
package com.gemstone.gemfire.test.junit.rules;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.gemstone.gemfire.test.junit.Retry;
import com.gemstone.gemfire.test.junit.categories.UnitTest;

/**
 * Unit tests for {@link RetryRule} involving local scope (ie rule affects
 * only the test methods annotated with {@code @Retry}) with failures due to
 * an {@code Exception}.
 *
 * @author Kirk Lund
 * @see com.gemstone.gemfire.test.junit.Retry
 * @see com.gemstone.gemfire.test.junit.rules.RetryRule
 */
@Category(UnitTest.class)
public class RetryRuleLocalWithExceptionTest {

  @Test
  public void failsUnused() {
    Result result = TestRunner.runTest(FailsUnused.class);
    
    assertThat(result.wasSuccessful()).isFalse();
    
    List<Failure> failures = result.getFailures();
    assertThat(failures.size()).as("Failures: " + failures).isEqualTo(1);

    Failure failure = failures.get(0);
    assertThat(failure.getException()).isExactlyInstanceOf(CustomException.class).hasMessage(FailsUnused.message);
    assertThat(FailsUnused.count).isEqualTo(1);
  }
  
  @Test
  public void passesUnused() {
    Result result = TestRunner.runTest(PassesUnused.class);
    
    assertThat(result.wasSuccessful()).isTrue();
    assertThat(PassesUnused.count).isEqualTo(1);
  }
  
  @Test
  public void failsOnSecondAttempt() {
    Result result = TestRunner.runTest(FailsOnSecondAttempt.class);
    
    assertThat(result.wasSuccessful()).isFalse();
    
    List<Failure> failures = result.getFailures();
    assertThat(failures.size()).as("Failures: " + failures).isEqualTo(1);

    Failure failure = failures.get(0);
    assertThat(failure.getException()).isExactlyInstanceOf(CustomException.class).hasMessage(FailsOnSecondAttempt.message);
    assertThat(FailsOnSecondAttempt.count).isEqualTo(2);
  }

  @Test
  public void passesOnSecondAttempt() {
    Result result = TestRunner.runTest(PassesOnSecondAttempt.class);
    
    assertThat(result.wasSuccessful()).isTrue();
    assertThat(PassesOnSecondAttempt.count).isEqualTo(2);
  }
  
  @Test
  public void failsOnThirdAttempt() {
    Result result = TestRunner.runTest(FailsOnThirdAttempt.class);
    
    assertThat(result.wasSuccessful()).isFalse();
    
    List<Failure> failures = result.getFailures();
    assertThat(failures.size()).as("Failures: " + failures).isEqualTo(1);

    Failure failure = failures.get(0);
    assertThat(failure.getException()).isExactlyInstanceOf(CustomException.class).hasMessage(FailsOnThirdAttempt.message);
    assertThat(FailsOnThirdAttempt.count).isEqualTo(3);
  }

  @Test
  public void passesOnThirdAttempt() {
    Result result = TestRunner.runTest(PassesOnThirdAttempt.class);
    
    assertThat(result.wasSuccessful()).isTrue();
    assertThat(PassesOnThirdAttempt.count).isEqualTo(3);
  }
  
  public static class CustomException extends Exception {
    private static final long serialVersionUID = 1L;
    public CustomException(final String message) {
      super(message);
    }
  }
  
  public static class FailsUnused {
    protected static int count;
    protected static String message;

    @Rule
    public RetryRule retryRule = new RetryRule();

    @Test
    public void failsUnused() throws Exception {
      count++;
      message = "Failing " + count;
      throw new CustomException(message);
    }
  }
  
  public static class PassesUnused {
    protected static int count;
    protected static String message;

    @Rule
    public RetryRule retryRule = new RetryRule();

    @Test
    public void passesUnused() throws Exception {
      count++;
    }
  }
  
  public static class FailsOnSecondAttempt {
    protected static int count;
    protected static String message;
    
    @Rule
    public RetryRule retryRule = new RetryRule();

    @Test
    @Retry(2)
    public void failsOnSecondAttempt() throws Exception {
      count++;
      message = "Failing " + count;
      throw new CustomException(message);
    }
  }
  
  public static class PassesOnSecondAttempt {
    protected static int count;
    protected static String message;
    
    @Rule
    public RetryRule retryRule = new RetryRule();

    @Test
    @Retry(2)
    public void failsOnSecondAttempt() throws Exception {
      count++;
      if (count < 2) {
        message = "Failing " + count;
        throw new CustomException(message);
      }
    }
  }
  
  public static class FailsOnThirdAttempt {
    protected static int count;
    protected static String message;
    
    @Rule
    public RetryRule retryRule = new RetryRule();

    @Test
    @Retry(3)
    public void failsOnThirdAttempt() throws Exception {
      count++;

      message = "Failing " + count;
      throw new CustomException(message);
    }
  }

  public static class PassesOnThirdAttempt {
    protected static int count;
    protected static String message;
    
    @Rule
    public RetryRule retryRule = new RetryRule();

    @Test
    @Retry(3)
    public void failsOnThirdAttempt() throws Exception {
      count++;

      if (count < 3) {
        message = "Failing " + count;
        throw new CustomException(message);
      }
    }
  }
}
