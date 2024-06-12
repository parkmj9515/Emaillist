<%@page import="himedia.dao.EmaillistDaoOracleImpl"%>
<%@page import="himedia.dao.EmaillistDao"%>
<%@ page import="java.sql.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
//	데이터베이스 접속 정보 확인
ServletContext context = getServletContext();

String dbuser = context.getInitParameter("dbuser");
String dbpass = context.getInitParameter("dbpass");

//폼 입력 데이터
long no = Integer.valueOf(request.getParameter("no"));	//	pk

EmaillistDao dao = new EmaillistDaoOracleImpl(dbuser, dbpass);
boolean success = dao.delete(no);

if (success) {
	response.sendRedirect(request.getContextPath() + "/emaillist/");	
} else {
		%>
		<h1>Error</h1>
		<p>아이템을 삭제하지 못했습니다.</p>
		<%
}
%>