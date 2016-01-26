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
/**
 * 
 */
package com.gemstone.gemfire.test.dunit;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.gemstone.gemfire.LogWriter;
import com.gemstone.gemfire.distributed.internal.DistributionConfig;
import com.gemstone.gemfire.distributed.internal.DistributionConfigImpl;
import com.gemstone.gemfire.internal.logging.LogWriterFactory;
import com.gemstone.gemfire.internal.logging.ManagerLogWriter;
import com.gemstone.gemfire.test.dunit.standalone.BounceResult;

/**
 * This class provides an abstraction over the environment
 * that is used to run dunit. This will delegate to the hydra
 * or to the standalone dunit launcher as needed.
 * 
 * Any dunit tests that rely on hydra configuration should go
 * through here, so that we can separate them out from depending on hydra
 * and run them on a different VM launching system.
 *   
 * @author dsmith
 *
 */
public abstract class DUnitEnv {
  
  public static DUnitEnv instance = null;
  
  public static final DUnitEnv get() {
    if (instance == null) {
      try {
        // for tests that are still being migrated to the open-source
        // distributed unit test framework  we need to look for this
        // old closed-source dunit environment
        Class<?> clazz = Class.forName("dunit.hydra.HydraDUnitEnv");
        instance = (DUnitEnv)clazz.newInstance();
      } catch (Exception e) {
        throw new Error("Distributed unit test environment is not initialized");
      }
    }
    return instance;
  }
  
  public static void set(DUnitEnv dunitEnv) {
    instance = dunitEnv;
  }
  
  public abstract String getLocatorString();
  
  public abstract String getLocatorAddress();

  public abstract int getLocatorPort();
  
  public abstract Properties getDistributedSystemProperties();

  public abstract int getPid();

  public abstract int getVMID();

  public abstract BounceResult bounce(int pid) throws RemoteException;

  public abstract File getWorkingDirectory(int pid);

  /**
   * Fetches the GemFireDescription for this test and adds its 
   * DistributedSystem properties to the provided props parameter.
   * 
   * @param config the properties to add hydra's test properties to
   */
  public static void addHydraProperties(Properties config) {
    Properties p = get().getDistributedSystemProperties();
    for (Iterator iter = p.entrySet().iterator();
        iter.hasNext(); ) {
      Map.Entry entry = (Map.Entry) iter.next();
      String key = (String) entry.getKey();
      String value = (String) entry.getValue();
      if (config.getProperty(key) == null) {
        config.setProperty(key, value);
      }
    }
  }

  /**
   * Creates a new LogWriter and adds it to the config properties. The config
   * can then be used to connect to DistributedSystem, thus providing early
   * access to the LogWriter before connecting. This call does not connect
   * to the DistributedSystem. It simply creates and returns the LogWriter
   * that will eventually be used by the DistributedSystem that connects using
   * config.
   * 
   * @param config the DistributedSystem config properties to add LogWriter to
   * @return early access to the DistributedSystem LogWriter
   */
  public static LogWriter createLogWriter(Properties config) { // LOG:CONVERT: this is being used for ExpectedExceptions
    Properties nonDefault = config;
    if (nonDefault == null) {
      nonDefault = new Properties();
    }
    DUnitEnv.addHydraProperties(nonDefault);
    
    DistributionConfig dc = new DistributionConfigImpl(nonDefault);
    LogWriter logger = LogWriterFactory.createLogWriterLogger(
        false/*isLoner*/, false/*isSecurityLog*/, dc, 
        false);        
    
    // if config was non-null, then these will be added to it...
    nonDefault.put(DistributionConfig.LOG_WRITER_NAME, logger);
    
    return logger;
  }

  public final static Properties getAllDistributedSystemProperties(Properties props) { // TODO: delete
    Properties dsProps = get().getDistributedSystemProperties();
    
    // our tests do not expect auto-reconnect to be on by default
    if (!dsProps.contains(DistributionConfig.DISABLE_AUTO_RECONNECT_NAME)) {
      dsProps.put(DistributionConfig.DISABLE_AUTO_RECONNECT_NAME, "true");
    }
  
    for (Iterator<Map.Entry<Object, Object>> iter = (Iterator<Map.Entry<Object, Object>>)props.entrySet().iterator(); iter.hasNext(); ) {
      Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iter.next();
      String key = (String) entry.getKey();
      Object value = entry.getValue();
      dsProps.put(key, value);
    }
    return dsProps;
  }
  
  /**
   * Get the port that the standard dunit locator is listening on.
   * @return
   */
  public static int getDUnitLocatorPort() {
    return get().getLocatorPort();
  }

  /**
   * This finds the log level configured for the test run.  It should be used
   * when creating a new distributed system if you want to specify a log level.
   * @return the dunit log-level setting
   */
  public static String getDUnitLogLevel() {
    Properties p = get().getDistributedSystemProperties();
    String result = p.getProperty(DistributionConfig.LOG_LEVEL_NAME);
    if (result == null) {
      result = ManagerLogWriter.levelToString(DistributionConfig.DEFAULT_LOG_LEVEL);
    }
    return result;
  }
}
