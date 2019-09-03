<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
   #memoForm{width: 420px; margin: auto;}
   .table{text-align: center; margin: auto;}
   .table th, td{padding: 5px;}
   .deleteButton{color: red;}
   .deleteButton:hover{cursor: pointer;}
</style>
<title>Insert title here</title>
</head>
<body>
   <c:import url="../common/menubar.jsp"/>
   
   <br><br>
   
    <form action="${contextPath}/memo/insertMemo.do" method="post" id="memoForm">
        <input type="text" name="memo" placeholder="메모" required/>&nbsp;
        <input type="password" name="password" maxlength="4" placeholder="비밀번호" required/>&nbsp;
        <button type="submit">저장</button>
    </form>
    
    <br><br>
    
    <!-- 메모목록 -->
    <table class="table">
        <tr>
            <th>번호</th>
            <th width="500px;">메모</th>
            <th width="300px;">날짜</th>
            <th>삭제</th>
        </tr>
        <c:forEach var="m" items="${ list }">
           <tr class="memo${m.MEMONO }">
              <td>${m.MEMONO }</td>
              <td>${m.MEMO }</td>
              <td>${m.MEMODATE }</td>
              <td><a class="deleteButton" onclick="deleteOk('${m.MEMONO}', '${m.PASSWORD}');">[삭제]</a></td>
           </tr>
        </c:forEach>
    </table>
    <script>
       function deleteOk(memono, password){
          var pass = prompt('비밀번호를 입력하세요');
          if(password == pass){
             $.ajax({
                 url: "delete.do",
                 data: {memono:memono},
                 type: "post",
                 success: function(data){
                    alert("성공적으로 삭제되었습니다.");
                    $(".memo" + memono).remove();
                 }, error: function(data){
                    alert("삭제에 실패했습니다.");
                 }
              });
          }else{
             alert("비밀번호가 틀립니다.");
          }
          
          
       }
    </script>
</body>
</html>