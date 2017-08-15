<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1">
    <title><spring:message code="home.title" /></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="resources/css/dailog.css" rel="stylesheet" type="text/css" />
    <link href="resources/css/calendar.css" rel="stylesheet" type="text/css" /> 
    <link href="resources/css/dp.css" rel="stylesheet" type="text/css" />   
    <link href="resources/css/alert.css" rel="stylesheet" type="text/css" /> 
    <link href="resources/css/main.css" rel="stylesheet" type="text/css" /> 
    <script src="resources/src/jquery.js" type="text/javascript"></script>  
    
    <script src="resources/src/Plugins/Common.js" type="text/javascript"></script>  
    <script src="resources/src/Plugins/datepicker_lang_ZH.js" type="text/javascript"></script> 
    <script src="resources/src/Plugins/jquery.datepicker.js" type="text/javascript"></script>

    <script src="resources/src/Plugins/jquery.alert.js" type="text/javascript"></script>    
    <script src="resources/src/Plugins/jquery.ifrmdailog.js" defer="defer" type="text/javascript"></script>
    
    <c:choose>  
	   <c:when test="${pageContext.response.locale == 'zh_CN'}">
	   <script src="resources/src/Plugins/wdCalendar_lang_ZH.js" type="text/javascript"></script>   
	   </c:when>  
	   <c:otherwise>
	   <script src="resources/src/Plugins/wdCalendar_lang_US.js" type="text/javascript"></script>     
	   </c:otherwise>  
	</c:choose>    
    <script src="resources/src/Plugins/jquery.calendar.js" type="text/javascript"></script>  
    <script src="resources/js/home.js" type="text/javascript"></script>   
</head>
<body>
	<div>
		<div id="calhead" style="padding-left: 1px; padding-right: 1px;">
			
			<div class="cHead">
				<div class="ftitle">
					<spring:message code="home.title" />
					<span style="float: right;">
						Language: <a href="?lang=zh_CN"><spring:message code="language.cn" /></a> - <a href="?lang=en_US"><spring:message code="language.en" /></a>
						| <a href="https://github.com/Jenner4S/wdCalendar_java" target="_blank">Github</a>
					</span>
				</div>
				
				<div id="loadingpannel" class="ptogtitle loadicon" style="display: none;"><spring:message code="home.loadingpannel.text"/></div>
				<div id="errorpannel" class="ptogtitle loaderror" style="display: none;"><spring:message code="home.errorpannel.text"/></div>
			</div>

			<div id="caltoolbar" class="ctoolbar">
				<div id="faddbtn" class="fbutton">
					<div>
						<span title='<spring:message code="home.addcal.title" />' class="addcal"> <spring:message code="home.addcal.text" /></span>
					</div>
				</div>
				<div class="btnseparator"></div>
				<div id="showtodaybtn" class="fbutton">
					<div>
						<span title='<spring:message code="home.showtoday.title" />' class="showtoday"> <spring:message code="home.showtoday.text" /></span>
					</div>
				</div>
				<div class="btnseparator"></div>

				<div id="showdaybtn" class="fbutton">
					<div>
						<span title='<spring:message code="home.showtoday.title" />' class="showdayview"><spring:message code="home.showtoday.text" /></span>
					</div>
				</div>
				<div id="showweekbtn" class="fbutton fcurrent">
					<div>
						<span title='<spring:message code="home.showweekview.text" />' class="showweekview"><spring:message code="home.showweekview.text" /></span>
					</div>
				</div>
				<div id="showmonthbtn" class="fbutton">
					<div>
						<span title='<spring:message code="home.showmonthview.text" />' class="showmonthview"><spring:message code="home.showmonthview.text" /></span>
					</div>

				</div>
				<div class="btnseparator"></div>
				<div id="showreflashbtn" class="fbutton">
					<div>
						<span title='<spring:message code="home.showdayflash.title" />' class="showdayflash"><spring:message code="home.showdayflash.text" /></span>
					</div>
				</div>
				<div class="btnseparator"></div>
				<div id="sfprevbtn" title="<spring:message code="home.fprev.title" />" class="fbutton">
					<span class="fprev"></span>

				</div>
				<div id="sfnextbtn" title="<spring:message code="home.fnext.title" />" class="fbutton">
					<span class="fnext"></span>
				</div>
				<div class="fshowdatep fbutton">
					<div>
						<input type="hidden" name="txtshow" id="hdtxtshow" /> 
						<span id="txtdatetimeshow"><spring:message code="home.txtdatetimeshow.text" /></span>
					</div>
				</div>

				<div class="clear"></div>
			</div>
		</div>
		<div style="padding: 1px;">
			<div class="t1 chromeColor">&nbsp;</div>
			<div class="t2 chromeColor">&nbsp;</div>
			<div id="dvCalMain" class="calmain printborder">
				<div id="gridcontainer" style="overflow-y: visible;" />
			</div>
			<div class="t2 chromeColor">&nbsp;</div>
			<div class="t1 chromeColor">&nbsp;</div>
		</div>
	</div>
</body>
</html>
