/*=========================================================================
 * Copyright (c) 2012-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * one or more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */
package com.vmware.gemfire.tools.pulse.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.JVM)
public class PulseTests {
 // private final static String jmxPropertiesFile = "E:\\springsource\\springsourceWS\\Pulse-Cedar\\src\\test\\resources\\test.properties";
  //private static String path = "D:\\springsource\\springsourceWS\\Pulse-Cedar\\build-artifacts\\win\\dist\\pulse-7.5.war";
  private final static String jmxPropertiesFile = System
      .getProperty("pulse.propfile");
  private static String path = System.getProperty("pulse.war");

  private static Tomcat tomcat = null;
  private static Server server = null;
  private static String pulseURL = null;
  public static WebDriver driver;
  private static final String userName = "admin";
  private static final String pasword = "admin";

  /* Constants for executing Data Browser queries */
  public static final String QUERY_TYPE_ONE = "query1";
  public static final String QUERY_TYPE_TWO = "query2";
  public static final String QUERY_TYPE_THREE = "query3";
  public static final String QUERY_TYPE_FOUR = "query4";
  public static final String QUERY_TYPE_FIVE = "query5";
  public static final String QUERY_TYPE_SIX = "query6";
  public static final String QUERY_TYPE_SEVENE = "query7";

  private static final String DATA_VIEW_LABEL = "Data View";
  private static final String CLUSTER_VIEW_MEMBERS_ID = "clusterTotalMembersText";
  private static final String CLUSTER_VIEW_SERVERS_ID = "clusterServersText";
  private static final String CLUSTER_VIEW_LOCATORS_ID = "clusterLocatorsText";
  private static final String CLUSTER_VIEW_REGIONS_ID = "clusterTotalRegionsText";
  private static final String CLUSTER_CLIENTS_ID = "clusterClientsText";
  private static final String CLUSTER_FUNCTIONS_ID = "clusterFunctions";
  private static final String CLUSTER_UNIQUECQS_ID = "clusterUniqueCQs";
  private static final String CLUSTER_SUBSCRIPTION_ID = "clusterSubscriptionsText";
  private static final String CLUSTER_MEMORY_USAGE_ID = "currentMemoryUsage";
  private static final String CLUSTER_THROUGHPUT_WRITES_ID = "currentThroughoutWrites";
  private static final String CLUSTER_GCPAUSES_ID = "currentGCPauses";
  private static final String CLUSTER_WRITEPERSEC_ID = "writePerSec";
  private static final String CLUSTER_READPERSEC_ID = "readPerSec";
  private static final String CLUSTER_QUERIESPERSEC_ID = "queriesPerSec";
  private static final String CLUSTER_PROCEDURE_ID = "clusterTxnCommittedText";
  private static final String CLUSTER_TXNCOMMITTED_ID = "clusterTxnCommittedText";
  private static final String CLUSTER_TXNROLLBACK_ID = "clusterTxnRollbackText";
  private static final String MEMBER_VIEW_MEMBERNAME_ID = "memberName";
  private static final String MEMBER_VIEW_REGION_ID = "memberRegionsCount";
  private static final String MEMBER_VIEW_THREAD_ID = "threads";
  private static final String MEMBER_VIEW_SOCKETS_ID = "sockets";
  private static final String MEMBER_VIEW_LOADAVG_ID = "loadAverage";
  private static final String MEMBER_VIEW_LISTENINGPORT_ID = "receiverListeningPort";
  private static final String MEMBER_VIEW_LINKTHROUGHPUT_ID = "receiverLinkThroughput";
  private static final String MEMBER_VIEW_AVGBATCHLATENCY_ID = "receiverAvgBatchLatency";
  private static final String MEMBER_VIEW_HEAPUSAGE_ID = "memberHeapUsageAvg";
  private static final String MEMBER_VIEW_JVMPAUSES_ID = "memberGcPausesAvg";
  private static final String MEMBER_VIEW_CPUUSAGE_ID = "memberCPUUsageValue";
  private static final String MEMBER_VIEW_READPERSEC_ID = "memberGetsPerSecValue";
  private static final String MEMBER_VIEW_WRITEPERSEC_ID = "memberPutsPerSecValue";
  private static final String MEMBER_VIEW_OFFHEAPFREESIZE_ID = "offHeapFreeSize";
  private static final String MEMBER_VIEW_OFFHEAPUSEDSIZE_ID = "offHeapUsedSize";
  private static final String MEMBER_VIEW_CLIENTS_ID = "clusterClientsText";

  private static final String REGION_NAME_LABEL = "regionName";
  private static final String REGION_PATH_LABEL = "regionPath";
  private static final String REGION_TYPE_LABEL = "regionType";
  private static final String DATA_VIEW_WRITEPERSEC = "regionWrites";
  private static final String DATA_VIEW_READPERSEC = "regionReads";
  private static final String DATA_VIEW_EMPTYNODES = "regionEmptyNodes";
  private static final String DATA_VIEW_ENTRYCOUNT = "regionEntryCount";
  private static final String REGION_PERSISTENCE_LABEL = "regionPersistence";
  private static final String DATA_VIEW_USEDMEMORY = "memoryUsed";
  private static final String DATA_VIEW_TOTALMEMORY = "totalMemory";
  
  private static final String DATA_BROWSER_LABEL = "Data Browser";
  private static final String DATA_BROWSER_REGIONName1 = "treeDemo_1_span";
  private static final String DATA_BROWSER_REGIONName2 = "treeDemo_2_span";
  private static final String DATA_BROWSER_REGIONName3 = "treeDemo_3_span";
  private static final String DATA_BROWSER_REGION1_CHECKBOX = "treeDemo_1_check";
  private static final String DATA_BROWSER_REGION2_CHECKBOX = "treeDemo_2_check";
  private static final String DATA_BROWSER_REGION3_CHECKBOX = "treeDemo_3_check";
  private static final String DATA_BROWSER_COLOCATED_REGION = "Colocated Regions";
  private static final String DATA_BROWSER_COLOCATED_REGION_NAME1 = "treeDemo_1_span";
  private static final String DATA_BROWSER_COLOCATED_REGION_NAME2 = "treeDemo_2_span";
  private static final String DATA_BROWSER_COLOCATED_REGION_NAME3 = "treeDemo_3_span";

  private static final String QUERY_STATISTICS_LABEL = "Query Statistics";
  private static final String CLUSTER_VIEW_LABEL = "Cluster View";
  private static final String CLUSTER_VIEW_GRID_ID = "default_treemap_button";
  private static final String SERVER_GROUP_GRID_ID = "servergroups_treemap_button";
  private static final String REDUNDANCY_GRID_ID = "redundancyzones_treemap_button";
  private static final String MEMBER_DROPDOWN_ID = "Members";
  private static final String DATA_DROPDOWN_ID = "Data";

  private static WebDriver initdriver = null;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    try {
      server = Server.createServer(9999, jmxPropertiesFile);

      String host = "localhost";// InetAddress.getLocalHost().getHostAddress();
      int port = 8080;
      String context = "/pulse";
      
      tomcat = TomcatHelper.startTomcat(host, port, context, path);
      pulseURL = "http://" + host + ":" + port + context;

      Thread.sleep(5000); // wait till tomcat settles down
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      Assert.fail("Error " + e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      Assert.fail("Error " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Error " + e.getMessage());
    }

    driver = new FirefoxDriver();
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    driver.get(pulseURL);
    WebElement userNameElement = driver.findElement(By.id("user_name"));
    WebElement passwordElement = driver.findElement(By.id("user_password"));
    userNameElement.sendKeys(userName);
    passwordElement.sendKeys(pasword);
    passwordElement.submit();

    Thread.sleep(3000);
    WebElement userNameOnPulsePage = (new WebDriverWait(driver, 10))
        .until(new ExpectedCondition<WebElement>() {
          @Override
          public WebElement apply(WebDriver d) {
            return d.findElement(By.id("userName"));
          }
        });
    Assert.assertNotNull(userNameOnPulsePage);
    driver.navigate().refresh();
    Thread.sleep(7000);
  }

  protected void searchByLinkAndClick(String linkText) {
    WebElement element = By.linkText(linkText).findElement(driver);
    Assert.assertNotNull(element);
    element.click();
  }

  protected void searchByIdAndClick(String id) {
    WebElement element = driver.findElement(By.id(id));
    Assert.assertNotNull(element);
    element.click();
  }

  protected void searchByClassAndClick(String Class) {
    WebElement element = driver.findElement(By.className(Class));
    Assert.assertNotNull(element);
    element.click();
  }

  protected void searchByXPathAndClick(String xpath) {
	WebElement element = driver.findElement(By.xpath(xpath));
     Assert.assertNotNull(element);
    element.click();
  }

  protected void waitForElementByClassName(final String className, int seconds) {
    WebElement linkTextOnPulsePage1 = (new WebDriverWait(driver, seconds))
        .until(new ExpectedCondition<WebElement>() {
          @Override
          public WebElement apply(WebDriver d) {
            return d.findElement(By.className(className));
          }
        });
    Assert.assertNotNull(linkTextOnPulsePage1);
  }

  protected void waitForElementById(final String id, int seconds) {
    WebElement element = (new WebDriverWait(driver, 10))
        .until(new ExpectedCondition<WebElement>() {
          @Override
          public WebElement apply(WebDriver d) {
            return d.findElement(By.id(id));
          }
        });
    Assert.assertNotNull(element);
  }
  
  protected void scrollbarVerticalDownScroll() {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("javascript:window.scrollBy(250,700)");
    WebElement pickerScroll = driver.findElement(By.className("jspDrag"));
    WebElement pickerScrollCorner = driver.findElement(By
        .className("jspCorner"));
    Actions builder = new Actions(driver);
    Actions movePicker = builder.dragAndDrop(pickerScroll, pickerScrollCorner); // pickerscroll
                                                                                // is
                                                                                // the
                                                                                // webelement
    movePicker.perform();
  }

  protected void scrollbarHorizontalRightScroll() {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("javascript:window.scrollBy(250,700)");
    WebElement pickerScroll = driver
        .findElement(By
            .xpath("//div[@id='gview_queryStatisticsList']/div[3]/div/div[3]/div[2]/div"));
    WebElement pickerScrollCorner = driver.findElement(By
        .className("jspCorner"));
    Actions builder = new Actions(driver);
    Actions movePicker = builder.dragAndDrop(pickerScroll, pickerScrollCorner); // pickerscroll
                                                                                // is
                                                                                // the
                                                                                // webelement
    movePicker.perform();
  }

  
  
  @Test
  public void testClusterLocatorCount() throws IOException {
	searchByXPathAndClick(PulseTestLocators.TopNavigation.clusterViewLinkXpath);
    String clusterLocators = driver
        .findElement(By.id(CLUSTER_VIEW_LOCATORS_ID)).getText();
   
    String totallocators = JMXProperties.getInstance().getProperty("server.S1.locatorCount");  
    Assert.assertEquals(totallocators, clusterLocators);
  }

 @Test
  public void testClusterRegionCount() {
    String clusterRegions = driver.findElement(By.id(CLUSTER_VIEW_REGIONS_ID))
        .getText();
    String totalregions = JMXProperties.getInstance().getProperty(
        "server.S1.totalRegionCount");
    Assert.assertEquals(totalregions, clusterRegions);
  }

 @Test
  public void testClusterMemberCount() {
    String clusterMembers = driver.findElement(By.id(CLUSTER_VIEW_MEMBERS_ID))
        .getText();
    String totalMembers = JMXProperties.getInstance().getProperty(
        "server.S1.memberCount");
    Assert.assertEquals(totalMembers, clusterMembers);
  }

 @Test
  public void testClusterNumClient() {
    String clusterClients = driver.findElement(By.id(CLUSTER_CLIENTS_ID))
        .getText();
    String totalclients = JMXProperties.getInstance().getProperty(
        "server.S1.numClients");
    Assert.assertEquals(totalclients, clusterClients);
  }

  @Test
  public void testClusterNumRunningFunction() {
    String clusterFunctions = driver.findElement(By.id(CLUSTER_FUNCTIONS_ID))
        .getText();
    String totalfunctions = JMXProperties.getInstance().getProperty(
        "server.S1.numRunningFunctions");
    Assert.assertEquals(totalfunctions, clusterFunctions);
  }

@Test
  public void testClusterRegisteredCQCount() {
    String clusterUniqueCQs = driver.findElement(By.id(CLUSTER_UNIQUECQS_ID))
        .getText();
    String totaluniqueCQs = JMXProperties.getInstance().getProperty(
        "server.S1.registeredCQCount");
    Assert.assertEquals(totaluniqueCQs, clusterUniqueCQs);
  }

 @Test
  public void testClusterNumSubscriptions() {
    String clusterSubscriptions = driver.findElement(
        By.id(CLUSTER_SUBSCRIPTION_ID)).getText();
    String totalSubscriptions = JMXProperties.getInstance().getProperty(
        "server.S1.numSubscriptions");
    Assert.assertEquals(totalSubscriptions, clusterSubscriptions);
  }

 @Test
  public void testClusterJVMPausesWidget() {
    String clusterJVMPauses = driver.findElement(By.id(CLUSTER_GCPAUSES_ID))
        .getText();
    String totalgcpauses = JMXProperties.getInstance().getProperty(
        "server.S1.jvmPauses");
    Assert.assertEquals(totalgcpauses, clusterJVMPauses);
  }

  @Test
  public void testClusterAverageWritesWidget() {
    String clusterWritePerSec = driver.findElement(
        By.id(CLUSTER_WRITEPERSEC_ID)).getText();
    String totalwritepersec = JMXProperties.getInstance().getProperty(
        "server.S1.averageWrites");
    Assert.assertEquals(totalwritepersec, clusterWritePerSec);
  }

  @Test
  public void testClusterAverageReadsWidget() {
    String clusterReadPerSec = driver.findElement(By.id(CLUSTER_READPERSEC_ID))
        .getText();
    String totalreadpersec = JMXProperties.getInstance().getProperty(
        "server.S1.averageReads");
    Assert.assertEquals(totalreadpersec, clusterReadPerSec);
  }

  @Test
  public void testClusterQuerRequestRateWidget() {
    String clusterQueriesPerSec = driver.findElement(
        By.id(CLUSTER_QUERIESPERSEC_ID)).getText();
    String totalqueriespersec = JMXProperties.getInstance().getProperty(
        "server.S1.queryRequestRate");
    Assert.assertEquals(totalqueriespersec, clusterQueriesPerSec);
  }
  
  @Test
  public void testClusterGridViewMemberID() throws InterruptedException {
	  
	 searchByIdAndClick("default_grid_button");	
	 List<WebElement> elements = driver.findElements(By.xpath("//table[@id='memberList']/tbody/tr")); //gives me 11 rows
	 
	 for(int memberCount = 1; memberCount<elements.size(); memberCount++){		  
		  String memberId = driver.findElement(By.xpath("//table[@id='memberList']/tbody/tr[" + (memberCount + 1) + "]/td")).getText();		  
		  String propertMemeberId= JMXProperties.getInstance().getProperty("member.M" + memberCount + ".id");		  
		  Assert.assertEquals(memberId, propertMemeberId);
	  }	 
  }

  @Test
  public void testClusterGridViewMemberName() {
	  searchByIdAndClick("default_grid_button"); 
	  List<WebElement> elements = driver.findElements(By.xpath("//table[@id='memberList']/tbody/tr"));  	  
	  for (int memberNameCount = 1; memberNameCount < elements.size(); memberNameCount++) {
		  String gridMemberName = driver.findElement(By.xpath("//table[@id='memberList']/tbody/tr[" + (memberNameCount + 1) + "]/td[2]")).getText();
		  String memberName = JMXProperties.getInstance().getProperty("member.M" + memberNameCount + ".member");
		  Assert.assertEquals(gridMemberName, memberName);
    }
  }
  

  @Test
  public void testClusterGridViewMemberHost() {
	  searchByIdAndClick("default_grid_button"); 
	  List<WebElement> elements = driver.findElements(By.xpath("//table[@id='memberList']/tbody/tr")); 	  
    for (int memberHostCount = 1; memberHostCount < elements.size(); memberHostCount++) {
      String MemberHost = driver.findElement(By.xpath("//table[@id='memberList']/tbody/tr[" + (memberHostCount + 1) + "]/td[3]")).getText();     
      String gridMemberHost = JMXProperties.getInstance().getProperty("member.M" + memberHostCount + ".host");
      Assert.assertEquals(gridMemberHost, MemberHost);
    }
  }

  @Test
  public void testClusterGridViewHeapUsage() {
	searchByIdAndClick("default_grid_button"); 
    for (int i = 1; i <= 3; i++) {
      Float HeapUsage = Float.parseFloat(driver
          .findElement(
              By.xpath("//table[@id='memberList']/tbody/tr[" + (i + 1) + "]/td[4]")).getText());
      Float gridHeapUsagestring = Float.parseFloat(JMXProperties.getInstance()
          .getProperty("member.M" + i + ".UsedMemory"));
     Assert.assertEquals(gridHeapUsagestring, HeapUsage);
    }
  }
   
  @Test
  public void testClusterGridViewCPUUsage() {
	searchByIdAndClick("default_grid_button"); 
    for (int i = 1; i <= 3; i++) {
      String CPUUsage = driver.findElement(By.xpath("//table[@id='memberList']/tbody/tr[" + (i + 1) + "]/td[5]")).getText();
      String gridCPUUsage = JMXProperties.getInstance().getProperty("member.M" + i + ".cpuUsage");
      gridCPUUsage = gridCPUUsage.trim();
      Assert.assertEquals(gridCPUUsage, CPUUsage);
    }
  }


  public void testRgraphWidget() throws InterruptedException {
    searchByIdAndClick("default_rgraph_button");
    Thread.sleep(7000);
    searchByIdAndClick("h1");
    Thread.sleep(500);
    searchByIdAndClick("M1");
    Thread.sleep(7000);
  }

  @Test  // region count in properties file is 2 and UI is 1
  public void testMemberTotalRegionCount() throws InterruptedException{
	testRgraphWidget();
    String RegionCount = driver.findElement(By.id(MEMBER_VIEW_REGION_ID)).getText();  
    String memberRegionCount = JMXProperties.getInstance().getProperty("member.M1.totalRegionCount");
    Assert.assertEquals(memberRegionCount, RegionCount);
  }

  @Test
  public void testMemberNumThread()throws InterruptedException {
    String ThreadCount = driver.findElement(By.id(MEMBER_VIEW_THREAD_ID)).getText();    
    String memberThreadCount = JMXProperties.getInstance().getProperty("member.M1.numThreads");   
    Assert.assertEquals(memberThreadCount, ThreadCount);
  }

  @Test
  public void testMemberTotalFileDescriptorOpen() throws InterruptedException {
    String SocketCount = driver.findElement(By.id(MEMBER_VIEW_SOCKETS_ID))
        .getText();
    String memberSocketCount = JMXProperties.getInstance().getProperty(
        "member.M1.totalFileDescriptorOpen");
    Assert.assertEquals(memberSocketCount, SocketCount);
  }

 @Test
  public void testMemberLoadAverage() throws InterruptedException {	
    String LoadAvg = driver.findElement(By.id(MEMBER_VIEW_LOADAVG_ID))
        .getText();
    String memberLoadAvg = JMXProperties.getInstance().getProperty(
        "member.M1.loadAverage");
    Assert.assertEquals(memberLoadAvg, LoadAvg);
  }

  @Ignore("WIP") // May be useful in near future
  @Test
  public void testOffHeapFreeSize(){	  
	  
    String OffHeapFreeSizeString = driver.findElement(
        By.id(MEMBER_VIEW_OFFHEAPFREESIZE_ID)).getText();
    String OffHeapFreeSizetemp = OffHeapFreeSizeString.replaceAll("[a-zA-Z]",
        "");
    float OffHeapFreeSize = Float.parseFloat(OffHeapFreeSizetemp);
    float memberOffHeapFreeSize = Float.parseFloat(JMXProperties.getInstance()
        .getProperty("member.M1.OffHeapFreeSize"));
    if (memberOffHeapFreeSize < 1048576) {
      memberOffHeapFreeSize = memberOffHeapFreeSize / 1024;

    } else if (memberOffHeapFreeSize < 1073741824) {
      memberOffHeapFreeSize = memberOffHeapFreeSize / 1024 / 1024;
    } else {
      memberOffHeapFreeSize = memberOffHeapFreeSize / 1024 / 1024 / 1024;
    }
    memberOffHeapFreeSize = Float.parseFloat(new DecimalFormat("##.##")
        .format(memberOffHeapFreeSize));
    Assert.assertEquals(memberOffHeapFreeSize, OffHeapFreeSize); 
 
  }

  @Ignore("WIP") // May be useful in near future
  @Test
  public void testOffHeapUsedSize() throws InterruptedException {
	 
    String OffHeapUsedSizeString = driver.findElement(
        By.id(MEMBER_VIEW_OFFHEAPUSEDSIZE_ID)).getText();
    String OffHeapUsedSizetemp = OffHeapUsedSizeString.replaceAll("[a-zA-Z]",
        "");
    float OffHeapUsedSize = Float.parseFloat(OffHeapUsedSizetemp);
    float memberOffHeapUsedSize = Float.parseFloat(JMXProperties.getInstance()
        .getProperty("member.M1.OffHeapUsedSize"));
    if (memberOffHeapUsedSize < 1048576) {
      memberOffHeapUsedSize = memberOffHeapUsedSize / 1024;

    } else if (memberOffHeapUsedSize < 1073741824) {
      memberOffHeapUsedSize = memberOffHeapUsedSize / 1024 / 1024;
    } else {
      memberOffHeapUsedSize = memberOffHeapUsedSize / 1024 / 1024 / 1024;
    }
    memberOffHeapUsedSize = Float.parseFloat(new DecimalFormat("##.##")
        .format(memberOffHeapUsedSize));
    Assert.assertEquals(memberOffHeapUsedSize, OffHeapUsedSize);
  }

  @Test
  public void testMemberJVMPauses(){
   
    String JVMPauses = driver.findElement(By.id(MEMBER_VIEW_JVMPAUSES_ID))
        .getText();
    String memberGcPausesAvg = JMXProperties.getInstance().getProperty(
        "member.M1.JVMPauses");
    Assert.assertEquals(memberGcPausesAvg, JVMPauses);
  }

  @Test
  public void testMemberCPUUsage() {  
    String CPUUsagevalue = driver.findElement(By.id(MEMBER_VIEW_CPUUSAGE_ID))
        .getText();
    String memberCPUUsage = JMXProperties.getInstance().getProperty(
        "member.M1.cpuUsage");
    Assert.assertEquals(memberCPUUsage, CPUUsagevalue);
  }

  @Test  // difference between UI and properties file
  public void testMemberAverageReads() {	  
    float ReadPerSec = Float.parseFloat(driver.findElement(By.id(MEMBER_VIEW_READPERSEC_ID)).getText());    
    float memberReadPerSec = Float.parseFloat(JMXProperties.getInstance().getProperty("member.M1.averageReads"));
    memberReadPerSec = Float.parseFloat(new DecimalFormat("##.##")
    .format(memberReadPerSec));
    Assert.assertEquals(memberReadPerSec, ReadPerSec);
  }

 @Test
  public void testMemberAverageWrites() throws InterruptedException {  
    String WritePerSec = driver.findElement(By.id(MEMBER_VIEW_WRITEPERSEC_ID))
        .getText();
    String memberWritePerSec = JMXProperties.getInstance().getProperty(
        "member.M1.averageWrites");
    Assert.assertEquals(memberWritePerSec, WritePerSec);
  }
 

 @Test
  public void testMemberGridViewData() throws InterruptedException {
   searchByXPathAndClick(PulseTestLocators.TopNavigation.clusterViewLinkXpath);
   testRgraphWidget();
   searchByXPathAndClick(PulseTestLocators.MemberDetailsView.gridButtonXpath);
   // get the number of rows on the grid
    List<WebElement> noOfRows = driver.findElements(By.xpath("//table[@id='memberRegionsList']/tbody/tr"));    
    String MemberRegionName = driver.findElement(By.xpath("//table[@id='memberRegionsList']/tbody/tr[2]/td[1]")).getText();
    String memberRegionName = JMXProperties.getInstance().getProperty("region.R1.name");
    Assert.assertEquals(memberRegionName, MemberRegionName);

    String MemberRegionType = driver.findElement(By.xpath("//table[@id='memberRegionsList']/tbody/tr[2]/td[2]")).getText();
    String memberRegionType = JMXProperties.getInstance().getProperty("region.R1.regionType");
    Assert.assertEquals(memberRegionType, MemberRegionType);
    
    String MemberRegionEntryCount = driver.findElement(By.xpath("//table[@id='memberRegionsList']/tbody/tr[2]/td[3]")).getText();
    String memberRegionEntryCount = JMXProperties.getInstance().getProperty("regionOnMember./R1.M1.entryCount");
    Assert.assertEquals(memberRegionEntryCount, MemberRegionEntryCount);
  }

@Test
  public void testDropDownList() throws InterruptedException {
	searchByIdAndClick("memberName");
    searchByLinkAndClick("M3");
    searchByIdAndClick("memberName");
    searchByLinkAndClick("M2");
  }

  @Ignore("WIP")
  @Test
  public void testDataViewRegionName() throws InterruptedException {
    searchByLinkAndClick(DATA_VIEW_LABEL);
    Thread.sleep(7000);
    searchByIdAndClick("default_grid_button");
    String regionName = driver.findElement(By.id(REGION_NAME_LABEL)).getText();
    String dataviewregionname = JMXProperties.getInstance().getProperty("region.R1.name");
    Assert.assertEquals(dataviewregionname, regionName);
  }

  @Ignore("WIP")
  @Test
  public void testDataViewRegionPath() {
    String regionPath = driver.findElement(By.id(REGION_PATH_LABEL)).getText();
    String dataviewregionpath = JMXProperties.getInstance().getProperty(
        "region.R1.fullPath");
    Assert.assertEquals(dataviewregionpath, regionPath);
  }

  @Ignore("WIP")
  @Test
  public void testDataViewRegionType() {
    String regionType = driver.findElement(By.id(REGION_TYPE_LABEL)).getText();
    String dataviewregiontype = JMXProperties.getInstance().getProperty(
        "region.R1.regionType");
    Assert.assertEquals(dataviewregiontype, regionType);
  }

  @Ignore("WIP")
  @Test
  public void testDataViewEmptyNodes() {
    String regionEmptyNodes = driver.findElement(By.id(DATA_VIEW_EMPTYNODES))
        .getText();
    String dataviewEmptyNodes = JMXProperties.getInstance().getProperty(
        "region.R1.emptyNodes");
    Assert.assertEquals(dataviewEmptyNodes, regionEmptyNodes);
  }

  @Ignore("WIP")
  @Test
  public void testDataViewSystemRegionEntryCount() {
    String regionEntryCount = driver.findElement(By.id(DATA_VIEW_ENTRYCOUNT))
        .getText();
    String dataviewEntryCount = JMXProperties.getInstance().getProperty(
        "region.R1.systemRegionEntryCount");
    Assert.assertEquals(dataviewEntryCount, regionEntryCount);
  }

  @Ignore("WIP")
  @Test
  public void testDataViewPersistentEnabled() {
    String regionPersistence = driver.findElement(
        By.id(REGION_PERSISTENCE_LABEL)).getText();
    String dataviewregionpersistence = JMXProperties.getInstance().getProperty(
        "region.R1.persistentEnabled");
    Assert.assertEquals(dataviewregionpersistence, regionPersistence);
  }

  @Ignore("WIP")
  @Test
  public void testDataViewDiskWritesRate() {
    String regionWrites = driver.findElement(By.id(DATA_VIEW_WRITEPERSEC))
        .getText();
    String dataviewRegionWrites = JMXProperties.getInstance().getProperty(
        "region.R1.diskWritesRate");
    Assert.assertEquals(dataviewRegionWrites, regionWrites);
  }

  @Ignore("WIP")
  @Test
  public void testDataViewDiskReadsRate() {
    String regionReads = driver.findElement(By.id(DATA_VIEW_READPERSEC))
        .getText();
    String dataviewRegionReads = JMXProperties.getInstance().getProperty(
        "region.R1.diskReadsRate");
    Assert.assertEquals(dataviewRegionReads, regionReads);
  }

  @Ignore("WIP")
  @Test
  public void testDataViewDiskUsage() {
    String regionMemoryUsed = driver.findElement(By.id(DATA_VIEW_USEDMEMORY))
        .getText();
    String dataviewMemoryUsed = JMXProperties.getInstance().getProperty(
        "region.R1.diskUsage");
    Assert.assertEquals(dataviewMemoryUsed, regionMemoryUsed);
    searchByLinkAndClick(QUERY_STATISTICS_LABEL);
  }

  @Ignore("WIP")
  @Test
  public void testDataViewGridValue() {
    String DataViewRegionName = driver.findElement(
        By.xpath("//*[id('6')/x:td[1]]")).getText();
    String dataViewRegionName = JMXProperties.getInstance().getProperty(
        "region.R1.name");
    Assert.assertEquals(dataViewRegionName, DataViewRegionName);

    String DataViewRegionType = driver.findElement(
        By.xpath("//*[id('6')/x:td[2]")).getText();
    String dataViewRegionType = JMXProperties.getInstance().getProperty(
        "region.R2.regionType");
    Assert.assertEquals(dataViewRegionType, DataViewRegionType);

    String DataViewEntryCount = driver.findElement(
        By.xpath("//*[id('6')/x:td[3]")).getText();
    String dataViewEntryCount = JMXProperties.getInstance().getProperty(
        "region.R2.systemRegionEntryCount");
    Assert.assertEquals(dataViewEntryCount, DataViewEntryCount);

    String DataViewEntrySize = driver.findElement(
        By.xpath("//*[id('6')/x:td[4]")).getText();
    String dataViewEntrySize = JMXProperties.getInstance().getProperty(
        "region.R2.entrySize");
    Assert.assertEquals(dataViewEntrySize, DataViewEntrySize);

  }
  
  
  public void loadDataBrowserpage() {
	  searchByLinkAndClick(DATA_BROWSER_LABEL);
	  //Thread.sleep(7000);
  }
  
  @Test
  public void testDataBrowserRegionName() throws InterruptedException {
	  loadDataBrowserpage();
	  String DataBrowserRegionName1 = driver.findElement(By.id(DATA_BROWSER_REGIONName1))
			  .getText();
	  String databrowserRegionNametemp1 = JMXProperties.getInstance().getProperty(
		        "region.R1.name");
	  String databrowserRegionName1 = databrowserRegionNametemp1.replaceAll("[\\/]", "");
	  Assert.assertEquals(databrowserRegionName1, DataBrowserRegionName1);
	  
	  String DataBrowserRegionName2 = driver.findElement(By.id(DATA_BROWSER_REGIONName2))
			  .getText();
	  String databrowserRegionNametemp2 = JMXProperties.getInstance().getProperty(
		        "region.R2.name");
	  String databrowserRegionName2 = databrowserRegionNametemp2.replaceAll("[\\/]", "");
	  Assert.assertEquals(databrowserRegionName2, DataBrowserRegionName2);
	  
	  String DataBrowserRegionName3 = driver.findElement(By.id(DATA_BROWSER_REGIONName3))
			  .getText();
	  String databrowserRegionNametemp3 = JMXProperties.getInstance().getProperty(
		        "region.R3.name");
	  String databrowserRegionName3 = databrowserRegionNametemp3.replaceAll("[\\/]", "");
	  Assert.assertEquals(databrowserRegionName3, DataBrowserRegionName3);
	        
  }
  
  @Test
  public void testDataBrowserRegionMembersVerificaition() throws InterruptedException {
	  loadDataBrowserpage();
	  searchByIdAndClick(DATA_BROWSER_REGION1_CHECKBOX);
	  String DataBrowserMember1Name1 = driver.findElement(By.xpath("//label[@for='Member0']"))
			  .getText();
	  String DataBrowserMember1Name2 = driver.findElement(By.xpath("//label[@for='Member1']"))
			  .getText();
	  String DataBrowserMember1Name3 = driver.findElement(By.xpath("//label[@for='Member2']"))
			  .getText();
	  String databrowserMember1Names = JMXProperties.getInstance().getProperty(
		        "region.R1.members");
	  
	  String databrowserMember1Names1 = databrowserMember1Names.substring(0, 2);
	  Assert.assertEquals(databrowserMember1Names1, DataBrowserMember1Name1);
	  
	  String databrowserMember1Names2 = databrowserMember1Names.substring(3, 5);
	  Assert.assertEquals(databrowserMember1Names2, DataBrowserMember1Name2);
	  
	  String databrowserMember1Names3 = databrowserMember1Names.substring(6, 8);
	  Assert.assertEquals(databrowserMember1Names3, DataBrowserMember1Name3);
	  searchByIdAndClick(DATA_BROWSER_REGION1_CHECKBOX);
	  
	  searchByIdAndClick(DATA_BROWSER_REGION2_CHECKBOX);
	  String DataBrowserMember2Name1 = driver.findElement(By.xpath("//label[@for='Member0']"))
			  .getText();
	  String DataBrowserMember2Name2 = driver.findElement(By.xpath("//label[@for='Member1']"))
			  .getText();
	  String databrowserMember2Names = JMXProperties.getInstance().getProperty(
		        "region.R2.members");
	  
	  String databrowserMember2Names1 = databrowserMember2Names.substring(0, 2);
	  Assert.assertEquals(databrowserMember2Names1, DataBrowserMember2Name1);
	  
	  String databrowserMember2Names2 = databrowserMember2Names.substring(3, 5);
	  Assert.assertEquals(databrowserMember2Names2, DataBrowserMember2Name2);
	  searchByIdAndClick(DATA_BROWSER_REGION2_CHECKBOX);
	  
	  searchByIdAndClick(DATA_BROWSER_REGION3_CHECKBOX);
	  String DataBrowserMember3Name1 = driver.findElement(By.xpath("//label[@for='Member0']"))
			  .getText();
	  String DataBrowserMember3Name2 = driver.findElement(By.xpath("//label[@for='Member1']"))
			  .getText();
	  String databrowserMember3Names = JMXProperties.getInstance().getProperty(
		        "region.R3.members");
	  
	  String databrowserMember3Names1 = databrowserMember3Names.substring(0, 2);
	  Assert.assertEquals(databrowserMember3Names1, DataBrowserMember3Name1);
	  
	  String databrowserMember3Names2 = databrowserMember3Names.substring(3, 5);
	  Assert.assertEquals(databrowserMember3Names2, DataBrowserMember3Name2);
	  searchByIdAndClick(DATA_BROWSER_REGION3_CHECKBOX);
  }
  
  @Test
  public void testDataBrowserColocatedRegions() throws InterruptedException {
	  loadDataBrowserpage();
	  String databrowserMemberNames1 = JMXProperties.getInstance().getProperty(
		        "region.R1.members");
	  String databrowserMemberNames2 = JMXProperties.getInstance().getProperty(
		        "region.R2.members");
	  String databrowserMemberNames3 = JMXProperties.getInstance().getProperty(
		        "region.R3.members");
	  
	  if((databrowserMemberNames1.matches(databrowserMemberNames2+"(.*)"))) {
		  if((databrowserMemberNames1.matches(databrowserMemberNames3+"(.*)"))) {
			  if((databrowserMemberNames2.matches(databrowserMemberNames3+"(.*)"))) {
				  System.out.println("R1, R2 and R3 are colocated regions");
			  }   
		  }
	  }
	  searchByIdAndClick(DATA_BROWSER_REGION1_CHECKBOX);
	  searchByLinkAndClick(DATA_BROWSER_COLOCATED_REGION);
	  String DataBrowserColocatedRegion1 = driver.findElement(By.id(DATA_BROWSER_COLOCATED_REGION_NAME1))
			  .getText();
	  String DataBrowserColocatedRegion2 = driver.findElement(By.id(DATA_BROWSER_COLOCATED_REGION_NAME2))
			  .getText();
	  String DataBrowserColocatedRegion3 = driver.findElement(By.id(DATA_BROWSER_COLOCATED_REGION_NAME3))
			  .getText();
	  
	  String databrowserColocatedRegiontemp1 = JMXProperties.getInstance().getProperty(
		        "region.R1.name");
	  String databrowserColocatedRegion1 = databrowserColocatedRegiontemp1.replaceAll("[\\/]", "");
	  
	  String databrowserColocatedRegiontemp2 = JMXProperties.getInstance().getProperty(
		        "region.R2.name");
	  String databrowserColocatedRegion2 = databrowserColocatedRegiontemp2.replaceAll("[\\/]", "");
	  
	  String databrowserColocatedRegiontemp3 = JMXProperties.getInstance().getProperty(
		        "region.R3.name");
	  String databrowserColocatedRegion3 = databrowserColocatedRegiontemp3.replaceAll("[\\/]", "");
	  
	  Assert.assertEquals(databrowserColocatedRegion1, DataBrowserColocatedRegion1);
	  Assert.assertEquals(databrowserColocatedRegion2, DataBrowserColocatedRegion2);
	  Assert.assertEquals(databrowserColocatedRegion3, DataBrowserColocatedRegion3);
	  
  }

  @Ignore("WIP") // clusterDetails element not found on Data Browser page. No assertions in test
  @Test
  public void testDataBrowserQueryValidation() throws IOException, InterruptedException {
	  loadDataBrowserpage();
	  WebElement textArea = driver.findElement(By.id("dataBrowserQueryText"));
	  textArea.sendKeys("query1");
	  WebElement executeButton = driver.findElement(By.id("btnExecuteQuery"));
	  executeButton.click();
	  String QueryResultHeader1 = driver.findElement(By.xpath("//div[@id='clusterDetails']/div/div/span[@class='n-title']")).getText();
	  double count = 0,countBuffer=0,countLine=0;
	  String lineNumber = "";
	  String filePath = "E:\\springsource\\springsourceWS\\Pulse-Cedar\\src\\main\\resources\\testQueryResultSmall.txt";
	  BufferedReader br;
	  String line = "";
	  br = new BufferedReader(new FileReader(filePath));
	  while((line = br.readLine()) != null)
	  {
		  countLine++;
          String[] words = line.split(" ");

          for (String word : words) {
            if (word.equals(QueryResultHeader1)) {
              count++;
              countBuffer++;
            }
          }
	  }  
  }
  
 public void testTreeMapPopUpData(String S1, String gridIcon) {
	  for (int i = 1; i <=3; i++) {
		  searchByLinkAndClick(CLUSTER_VIEW_LABEL);
		  if (gridIcon.equals(SERVER_GROUP_GRID_ID)) {
			  WebElement ServerGroupRadio = driver.findElement(By.xpath("//label[@for='radio-servergroups']"));
			  ServerGroupRadio.click();
		  }
		  if (gridIcon.equals(REDUNDANCY_GRID_ID)) {
			  WebElement ServerGroupRadio = driver.findElement(By.xpath("//label[@for='radio-redundancyzones']"));
			  ServerGroupRadio.click();
		  }
		  searchByIdAndClick(gridIcon);
		  WebElement TreeMapMember = driver.findElement(By.xpath("//div[@id='" + S1 + "M"+ (i) + "']/div"));
		  Actions builder = new Actions(driver);
		  builder.clickAndHold(TreeMapMember).perform();
		  int j = 1;
		  String CPUUsageM1temp = driver.findElement(By.xpath("//div[@id='_tooltip']/div/div/div[2]/div/div[2]/div"))
				  .getText();
		  String CPUUsageM1 = CPUUsageM1temp.replaceAll("[\\%]", "");
		  String cpuUsageM1 = JMXProperties.getInstance().getProperty(
			        "member.M" + (i) + ".cpuUsage");
		  Assert.assertEquals(cpuUsageM1, CPUUsageM1);
			  
		  String MemoryUsageM1temp = driver.findElement(By.xpath("//div[@id='_tooltip']/div/div/div[2]/div[" + (j + 1) + "]/div[2]/div"))
				  .getText();
		  String MemoryUsageM1 = MemoryUsageM1temp.replaceAll("MB", "");
		  String memoryUsageM1 = JMXProperties.getInstance().getProperty(
				  "member.M" + (i) + ".UsedMemory");
		  Assert.assertEquals(memoryUsageM1, MemoryUsageM1);
		  
		  String LoadAvgM1 = driver.findElement(By.xpath("//div[@id='_tooltip']/div/div/div[2]/div[" + (j + 2) + "]/div[2]/div"))
				  .getText();
		  String loadAvgM1 = JMXProperties.getInstance().getProperty(
				  "member.M" + (i) + ".loadAverage");
		  Assert.assertEquals(loadAvgM1, LoadAvgM1);
		  
		  
		  String ThreadsM1 = driver.findElement(By.xpath("//div[@id='_tooltip']/div/div/div[2]/div[" + (j + 3) + "]/div[2]/div"))
				  .getText();
		  String threadsM1 = JMXProperties.getInstance().getProperty(
				  "member.M" + (i) + ".numThreads");
		  Assert.assertEquals(threadsM1, ThreadsM1);
		  
		  String SocketsM1 = driver.findElement(By.xpath("//div[@id='_tooltip']/div/div/div[2]/div[" + (j + 4) + "]/div[2]/div"))
				  .getText();
		  String socketsM1 = JMXProperties.getInstance().getProperty(
				  "member.M" + (i) + ".totalFileDescriptorOpen");
		  Assert.assertEquals(socketsM1, SocketsM1);
          builder.moveToElement(TreeMapMember).release().perform();
		  }
	  }
  
  @Test
  public void testTopologyPopUpData() {
	  testTreeMapPopUpData("", CLUSTER_VIEW_GRID_ID); 
  }
  
  @Test
  public void testServerGroupTreeMapPopUpData() {
	  testTreeMapPopUpData("SG1(!)", SERVER_GROUP_GRID_ID);
  }
  
  @Test
  public void testDataViewTreeMapPopUpData() {
	  searchByLinkAndClick(CLUSTER_VIEW_LABEL);
	  searchByLinkAndClick(DATA_DROPDOWN_ID);
	  WebElement TreeMapMember = driver.findElement(By.id("GraphTreeMapClusterData-canvas"));
	  Actions builder = new Actions(driver);
	  builder.clickAndHold(TreeMapMember).perform();
	  String RegionType = driver.findElement(By.xpath("//div[@id='_tooltip']/div/div/div[2]/div/div[2]/div"))
			  .getText();
	  String regionType = JMXProperties.getInstance().getProperty(
			  "region.R2.regionType");
	  Assert.assertEquals(regionType, RegionType);
	  
	  String EntryCount = driver.findElement(By.xpath("//div[@id='_tooltip']/div/div/div[2]/div[2]/div[2]/div"))
			  .getText();
	  String entryCount = JMXProperties.getInstance().getProperty(
			  "region.R2.systemRegionEntryCount");
	  Assert.assertEquals(entryCount, EntryCount);
	  
	  String EntrySizetemp = driver.findElement(By.xpath("//div[@id='_tooltip']/div/div/div[2]/div[3]/div[2]/div"))
			  .getText();
	  float EntrySize = Float.parseFloat(EntrySizetemp);
	  float entrySize = Float.parseFloat(JMXProperties.getInstance().getProperty(
			  "region.R2.entrySize"));
	  entrySize = entrySize / 1024 / 1024;
	  entrySize = Float.parseFloat(new DecimalFormat("##.####")
      .format(entrySize));
	  Assert.assertEquals(entrySize, EntrySize);  
	  builder.moveToElement(TreeMapMember).release().perform();
  }
  
  @Test
  public void testRegionViewTreeMapPopUpData() {
	  searchByLinkAndClick(CLUSTER_VIEW_LABEL);
	  searchByLinkAndClick(DATA_DROPDOWN_ID);
	  WebElement TreeMapMember = driver.findElement(By.id("GraphTreeMapClusterData-canvas"));
	  TreeMapMember.click();
  }

  @Ignore("WIP")
  @Test
  public void testNumberOfRegions() throws InterruptedException{
	  
		driver.findElement(By.xpath("//a[text()='Data Browser']")).click();
		
		 Thread.sleep(1000);
		 List<WebElement> regionList = driver.findElements(By.xpath("//ul[@id='treeDemo']/li"));		 
		 String regions = JMXProperties.getInstance().getProperty("regions");
		 String []regionName = regions.split(" ");
		 for (String string : regionName) {
		}
		 //JMXProperties.getInstance().getProperty("region.R1.regionType");
		int i=1; 
		for (WebElement webElement : regionList) {
			//webElement.getAttribute(arg0)
			i++;
		}
		
		driver.findElement(By.id("treeDemo_1_check")).click();		
		
		List<WebElement> memeberList = driver.findElements(By.xpath("//ul[@id='membersList']/li"));
		int j=0;
		for (WebElement webElement : memeberList) {
			j++;
		}  
  }

  @Ignore("WIP")
  @Test
  public void testDataBrowser(){
	  
	  driver.findElement(By.linkText("Data Browser")).click();
	 // WebElement dataBrowserLabel = driver.findElement(By.xpath(""));
	  WebDriverWait wait = new WebDriverWait(driver, 20);
	  wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//label[text()='Data Browser']"))));
	  
	
	// Verify all elements must be displayed on data browser screen 
	  Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Data Regions']")).isDisplayed());	
	  Assert.assertTrue(driver.findElement(By.id("linkColocatedRegions")).isDisplayed());	  
	  Assert.assertTrue(driver.findElement(By.linkText("All Regions")).isDisplayed());
	  
	  Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Region Members']")).isDisplayed());
	  
	  Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Queries']")).isDisplayed());
	  Assert.assertTrue(driver.findElement(By.xpath("//label[text()='Query Editor']")).isDisplayed());
	  Assert.assertTrue(driver.findElement(By.xpath("//label[text()='Result']")).isDisplayed());
	  Assert.assertTrue(driver.findElement(By.xpath("//input[@value='Export Result']")).isDisplayed());
	  Assert.assertTrue(driver.findElement(By.id("btnExecuteQuery")).isDisplayed());
	  Assert.assertTrue(driver.findElement(By.xpath("//input[@value='Clear']")).isDisplayed());
	  Assert.assertTrue(driver.findElement(By.id("dataBrowserQueryText")).isDisplayed());
	  
	  Assert.assertTrue(driver.findElement(By.id("historyIcon")).isDisplayed());
	  
	  //Acutal query execution
	  
	  driver.findElement(By.id("dataBrowserQueryText")).sendKeys("Query1");
	  
	  
	  
	 
	  // Assert data regions are displayed 
	  Assert.assertTrue(driver.findElement(By.id("treeDemo_1")).isDisplayed());
	
	  
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
	  //Thread.sleep(200000000);
    // driver.close();
    /*try {
      if (tomcat != null) {
        tomcat.stop();
        tomcat.destroy();
      }

      System.out.println("Tomcat Stopped");

      if (server != null) {
        server.stop();
      }

      System.out.println("Server Stopped");
    } catch (LifecycleException e) {
      e.printStackTrace();
    }
*/
  }

}
