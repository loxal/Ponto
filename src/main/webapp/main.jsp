<!DOCTYPE html>
<!--
~ Copyright 2012 digital publishing AG. All rights reserved.
-->
<title>Verp</title>
<%
    final String module;
    if (request.getParameter("module") == null) {
//        module = "dp.verp.trainer.Trainer"; // default GWT module
//        module = "dp.verp.student.Student"; // default GWT module
//        module = "dp.verp.actas.ActAsService"; // default GWT module
        module = "";
    } else {
        module = request.getParameter("module");
    }
%>
<script src="<%= module + "/" + module + ".nocache.js" %>"></script>
<iframe src="javascript:''" id="__gwt_historyFrame" style="width:0; height:0; border:0;"></iframe>

<%
    if (request.getParameter("uiTest") != null) {
%>
<div id="uiTest"></div>
<script type="application/dart" src="ui_test.dart"></script>
<%
    }
%>
