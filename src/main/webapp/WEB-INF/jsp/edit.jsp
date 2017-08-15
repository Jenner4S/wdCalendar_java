<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
   
  <head>    
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">    
    <title><spring:message code="edit.title"/></title>    
    <link href="resources/css/main.css" rel="stylesheet" type="text/css" />       
    <link href="resources/css/dp.css" rel="stylesheet" />    
    <link href="resources/css/dropdown.css" rel="stylesheet" />    
    <link href="resources/css/colorselect.css" rel="stylesheet" />   
       
    <script src="resources/src/jquery.js" type="text/javascript"></script>    
    <script src="resources/src/Plugins/Common.js" type="text/javascript"></script>        
    <script src="resources/src/Plugins/jquery.form.js" type="text/javascript"></script>     
    <script src="resources/src/Plugins/jquery.validate.js" type="text/javascript"></script>     
    <script src="resources/src/Plugins/datepicker_lang_ZH.js" type="text/javascript"></script> 
	<c:choose>  
	   <c:when test="${pageContext.response.locale == 'zh_CN'}">
	   <script src="resources/src/Plugins/wdCalendar_lang_ZH.js" type="text/javascript"></script>   
	   </c:when>  
	   <c:otherwise>
	   <script src="resources/src/Plugins/wdCalendar_lang_US.js" type="text/javascript"></script>     
	   </c:otherwise>  
	</c:choose> 
          
    <script src="resources/src/Plugins/jquery.datepicker.js" type="text/javascript"></script>     
    <script src="resources/src/Plugins/jquery.dropdown.js" type="text/javascript"></script>     
    <script src="resources/src/Plugins/jquery.colorselect.js" type="text/javascript"></script>  
    <script type="text/javascript">
        if (!DateAdd || typeof (DateDiff) != "function") {
            var DateAdd = function(interval, number, idate) {
                number = parseInt(number);
                var date;
                if (typeof (idate) == "string") {
                    date = idate.split(/\D/);
                    eval("var date = new Date(" + date.join(",") + ")");
                }
                if (typeof (idate) == "object") {
                    date = new Date(idate.toString());
                }
                switch (interval) {
                    case "y": date.setFullYear(date.getFullYear() + number); break;
                    case "m": date.setMonth(date.getMonth() + number); break;
                    case "d": date.setDate(date.getDate() + number); break;
                    case "w": date.setDate(date.getDate() + 7 * number); break;
                    case "h": date.setHours(date.getHours() + number); break;
                    case "n": date.setMinutes(date.getMinutes() + number); break;
                    case "s": date.setSeconds(date.getSeconds() + number); break;
                    case "l": date.setMilliseconds(date.getMilliseconds() + number); break;
                }
                return date;
            }
        }
        function getHM(date)
        {
             var hour =date.getHours();
             var minute= date.getMinutes();
             var ret= (hour>9?hour:"0"+hour)+":"+(minute>9?minute:"0"+minute) ;
             return ret;
        }
        $(document).ready(function() {
        		
            //debugger;
            var DATA_FEED_URL = "calendar/rest";
            var arrT = [];
            var tt = "{0}:{1}";
            for (var i = 0; i < 24; i++) {
                arrT.push({ text: StrFormat(tt, [i >= 10 ? i : "0" + i, "00"]) }, { text: StrFormat(tt, [i >= 10 ? i : "0" + i, "30"]) });
            }
            $("#timezone").val(new Date().getTimezoneOffset()/60 * -1);
            $("#stparttime").dropdown({
                dropheight: 200,
                dropwidth:60,
                selectedchange: function() { },
                items: arrT
            });
            $("#etparttime").dropdown({
                dropheight: 200,
                dropwidth:60,
                selectedchange: function() { },
                items: arrT
            });
            var check = $("#IsAllDayEvent").click(function(e) {
                if (this.checked) {
                    $("#stparttime").val("00:00").hide();
                    $("#etparttime").val("00:00").hide();
                }
                else {
                    var d = new Date();
                    var p = 60 - d.getMinutes();
                    if (p > 30) p = p - 30;
                    d = DateAdd("n", p, d);
                    $("#stparttime").val(getHM(d)).show();
                    $("#etparttime").val(getHM(DateAdd("h", 1, d))).show();
                }
            });
            if (check[0].checked) {
                $("#stparttime").val("00:00").hide();
                $("#etparttime").val("00:00").hide();
            }
            $("#Savebtn").click(function() { $("#fmEdit").submit(); });
            $("#Closebtn").click(function() { CloseModelWindow(); });
            $("#Deletebtn").click(function() {
                 if (confirm(i18n.xgcalendar.ConfirmStr)) {  
                    var param = [{ "name": "calendarId", value: 8}];                
                    $.post(DATA_FEED_URL + "?method=remove",param,function(data){
                        if (data.IsSuccess) {
                            alert(data.Msg); 
                            CloseModelWindow(null,true);                            
                        }
                        else {
                            alert("Error occurs.\r\n" + data.Msg);
                        }
                     },"json");
                }
            });
            
           $("#stpartdate,#etpartdate").datepicker({ picker: "<button class='calpick'></button>"});    
            var cv =$("#colorvalue").val() ;
            if(cv=="")
            {
                cv="-1";
            }
            $("#calendarcolor").colorselect({ title: "Color", index: cv, hiddenid: "colorvalue" });
            //to define parameters of ajaxform
            var options = {
                beforeSubmit: function() {
                    return true;
                },
                dataType: "json",
                success: function(data) {
                    alert(data.Msg);
                    if (data.IsSuccess) {
                        CloseModelWindow(null,true);  
                    }
                }
            };
            $.validator.addMethod("date", function(value, element) {                             
                var arrs = value.split(i18n.datepicker.dateformat.separator);
                var year = arrs[i18n.datepicker.dateformat.year_index];
                var month = arrs[i18n.datepicker.dateformat.month_index];
                var day = arrs[i18n.datepicker.dateformat.day_index];
                var standvalue = [year,month,day].join("-");
                return this.optional(element) || /^(?:(?:1[6-9]|[2-9]\d)?\d{2}[\/\-\.](?:0?[1,3-9]|1[0-2])[\/\-\.](?:29|30))(?: (?:0?\d|1\d|2[0-3])\:(?:0?\d|[1-5]\d)\:(?:0?\d|[1-5]\d)(?: \d{1,3})?)?$|^(?:(?:1[6-9]|[2-9]\d)?\d{2}[\/\-\.](?:0?[1,3,5,7,8]|1[02])[\/\-\.]31)(?: (?:0?\d|1\d|2[0-3])\:(?:0?\d|[1-5]\d)\:(?:0?\d|[1-5]\d)(?: \d{1,3})?)?$|^(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])[\/\-\.]0?2[\/\-\.]29)(?: (?:0?\d|1\d|2[0-3])\:(?:0?\d|[1-5]\d)\:(?:0?\d|[1-5]\d)(?: \d{1,3})?)?$|^(?:(?:16|[2468][048]|[3579][26])00[\/\-\.]0?2[\/\-\.]29)(?: (?:0?\d|1\d|2[0-3])\:(?:0?\d|[1-5]\d)\:(?:0?\d|[1-5]\d)(?: \d{1,3})?)?$|^(?:(?:1[6-9]|[2-9]\d)?\d{2}[\/\-\.](?:0?[1-9]|1[0-2])[\/\-\.](?:0?[1-9]|1\d|2[0-8]))(?: (?:0?\d|1\d|2[0-3])\:(?:0?\d|[1-5]\d)\:(?:0?\d|[1-5]\d)(?:\d{1,3})?)?$/.test(standvalue);
            }, "Invalid date format");
            $.validator.addMethod("time", function(value, element) {
                return this.optional(element) || /^([0-1]?[0-9]|2[0-3]):([0-5][0-9])$/.test(value);
            }, "Invalid time format");
            $.validator.addMethod("safe", function(value, element) {
                return this.optional(element) || /^[^$\<\>]+$/.test(value);
            }, "$<> not allowed");
            $("#fmEdit").validate({
                submitHandler: function(form) { $("#fmEdit").ajaxSubmit(options); },
                errorElement: "div",
                errorClass: "cusErrorPanel",
                errorPlacement: function(error, element) {
                    showerror(error, element);
                }
            });
            function showerror(error, target) {
                var pos = target.position();
                var height = target.height();
                var newpos = { left: pos.left, top: pos.top + height + 2 }
                var form = $("#fmEdit");             
                error.appendTo(form).css(newpos);
            }
        });
    </script>      
    <style type="text/css">     
    .calpick     {        
        width:16px;   
        height:16px;     
        border:none;        
        cursor:pointer;        
        background:url("resources/sample-css/cal.gif") no-repeat center 2px;        
        margin-left:-22px;    
    }      
    </style>
  </head>
  <body>    
    <div>      
      <div class="toolBotton">           
        <a id="Savebtn" class="imgbtn" href="javascript:void(0);">                
          <span class="Save"  title="<spring:message code="edit.Save.title"/>"><spring:message code="edit.Save.text"/>(<u>S</u>)
          </span>          
        </a>  
          
	   <c:if test="${not empty event}">
	     <a id="Deletebtn" class="imgbtn" href="javascript:void(0);">                    
	          <span class="Delete" title="<spring:message code="edit.Delete.title"/>"><spring:message code="edit.Delete.text"/>(<u>D</u>)
	          </span>                
	        </a>  
		</c:if>
        <a id="Closebtn" class="imgbtn" href="javascript:void(0);">                
          <span class="Close" title="<spring:message code="edit.Close.title"/>" ><spring:message code="edit.Close.text"/></span>         
        </a>        
      </div>                  
      <div style="clear: both">         
      </div>        
      <div class="infocontainer">            
<!--        <form action="php/datafeed.php?method=adddetails><?php echo isset($event)?"&id=".$event->Id:""; ?>" class="fform" id="fmEdit" method="post">                 -->
        <form action='calendar/rest?method=adddetails<c:if test="${not empty event}"><c:out value="&id=${event.id}"/></c:if>' class="fform" id="fmEdit" method="post">                 
          <label>                    
            <span>*<spring:message code="edit.Subject.text"/>:</span>                    
            <div id="calendarcolor">
            </div>
            <input MaxLength="200" class="required safe" id="Subject" name="Subject" style="width:85%;" type="text" value='<c:if test="${not empty event}"><c:out value="${event.subject}"/></c:if>' />                     
            <input id="colorvalue" name="colorvalue" type="hidden" value='<c:if test="${not empty event}"><c:out value="${event.color}"/></c:if>' />                
          </label>                 
          <label>                    
            <span>*<spring:message code="edit.Time.text"/>:
            </span>                    
            <div>  
          <input MaxLength="10" class="required date" id="stpartdate" name="stpartdate" style="padding-left:2px;width:90px;" type="text" value='<c:if test="${not empty stpartdate}"><c:out value="${stpartdate}"/></c:if>' />                       
              <input MaxLength="5" class="required time" id="stparttime" name="stparttime" style="width:40px;" type="text" value='<c:if test="${not empty stparttime}"><c:out value="${stparttime}"/></c:if>' /> 至                       
              <input MaxLength="10" class="required date" id="etpartdate" name="etpartdate" style="padding-left:2px;width:90px;" type="text" value='<c:if test="${not empty etpartdate}"><c:out value="${etpartdate}"/></c:if>' />                       
              <input MaxLength="50" class="required time" id="etparttime" name="etparttime" style="width:40px;" type="text" value='<c:if test="${not empty etparttime}"><c:out value="${etparttime}"/></c:if>' />                                            
              <label class="checkp"> 
                <input id="IsAllDayEvent" name="IsAllDayEvent" type="checkbox" value="1" 
                       <c:if test="${not empty event && event.isAllDayEvent != 0}"><c:out value="checked"/></c:if>
                /><spring:message code="edit.IsAllDayEvent.text"/>                   
              </label>                    
            </div>                
          </label>                 
          <label>                    
          	<span><spring:message code="edit.participant.text"/>:</span>
          	<select id="participant" name="participant" style="width:95%;">
          		<option>Select...</option>
          	<c:if test="${not empty users }">
          	<c:forEach items="${users }" var="user">
          		<option value="${user.id }" 
          		<c:if test="${not empty uid}">
	          		<c:if test="${uid == user.id}">
	          			selected
	          		</c:if>
          		</c:if>
          		>${user.userName }</option>
          	</c:forEach>	
          	</c:if>
          	
          	
          	</select>                    
          </label>
          <label>                    
          	<span><spring:message code="edit.Location.text"/>:</span>                    
          	<input MaxLength="200" id="Location" name="Location" style="width:95%;" type="text" value='<c:if test="${not empty event}"><c:out value="${event.location}"/></c:if>' />                 
          </label>                 
          <label>                    
            		<span><spring:message code="edit.Remark.text"/>:</span>                    
				<textarea cols="20" id="Description" name="Description" rows="2" style="width:95%; height:70px"><c:if test="${not empty event}"><c:out value="${event.description}"/></c:if></textarea>                
          </label>                
          <input id="timezone" name="timezone" type="hidden" value="" />   
          
          <input id="locale" name="locale" type="hidden" value="${pageContext.response.locale } " />   
        </form>         
      </div>         
    </div>
  </body>
</html>