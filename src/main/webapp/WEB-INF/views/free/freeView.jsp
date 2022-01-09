
<%@page import="com.study.exception.BizNotEffectedException"%>
<%@page import="com.study.exception.BizNotFoundException"%>
<%@page import="com.study.free.service.FreeBoardServiceImpl"%>
<%@page import="com.study.free.service.IFreeBoardService"%>
<%@page import="com.study.free.vo.FreeBoardVO"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/inc/header.jsp"%>
<title>자유게시판 - 글 보기</title>
</head>
<body>
	<%@ include file="/WEB-INF/inc/top.jsp"%>

	<c:if test="${enf !=null or nen != null}">
		<div class="alert alert-warning">해당 글이 존재하지 않습니다. 또는 조회수증가
			실패했습니다.</div>
		<a href="freeList.wow" class="btn btn-default btn-sm"> <span
			class="glyphicon glyphicon-list" aria-hidden="true"></span> &nbsp;목록
		</a>
	</c:if>

	<c:if test="${freeBoard!=null }">
		<div class="container">
			<div class="page-header">
				<h3>
					자유게시판 - <small>글 보기</small>
				</h3>
			</div>
			<table class="table table-striped table-bordered">
				<tbody>
					<tr>
						<th>글번호</th>
						<td>${freeBoard.boNo }</td>
					</tr>
					<tr>
						<th>글제목</th>
						<td>${freeBoard.boTitle }</td>
					</tr>
					<tr>
						<th>글분류</th>
						<td>${freeBoard.boCategoryNm}</td>
					</tr>
					<tr>
						<th>작성자명</th>
						<td>${freeBoard.boWriter }</td>
					</tr>
					<!-- 비밀번호는 보여주지 않음  -->
					<tr>
						<th>내용</th>
						<td><textarea rows="10" name="boContent"
								class="form-control input-sm">
						${freeBoard.boContent }
					</textarea></td>
					</tr>
					<tr>
						<th>등록자 IP</th>
						<td>${freeBoard.boIp }</td>
					</tr>
					<tr>
						<th>조회수</th>
						<td>${freeBoard.boHit }</td>
					</tr>
					<tr>
						<th>최근등록일자</th>
						<td>${freeBoard.boRegDate }</td>
					</tr>
					<tr>
						<th>첨부파일</th>
						<td><c:forEach var="f" items="${freeBoard.attaches}"
								varStatus="st">
								<div>
									파일 ${st.count} <a
										href="<c:url value='/attach/download/${f.atchNo}' />"
										target="_blank"> <span class="glyphicon glyphicon-save"
										aria-hidden="true"></span> ${f.atchOriginalName}
									</a> Size : ${f.atchFancySize} Down : ${f.atchDownHit}
								</div>
							</c:forEach></td>
					</tr>
					<tr>
						<th>삭제여부</th>
						<td>${freeBoard.boDelYn }</td>
					</tr>
					<tr>
						<td colspan="2">
							<div class="pull-left">
								<a href="freeList.wow" class="btn btn-default btn-sm"> <span
									class="glyphicon glyphicon-list" aria-hidden="true"></span>
									&nbsp;&nbsp;목록
								</a>
							</div>
							<div class="pull-right">
								<a href="freeEdit.wow?boNo=${freeBoard.boNo }"
									class="btn btn-success btn-sm"> <span
									class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
									&nbsp;&nbsp;수정
								</a>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="container">
			<!-- reply container -->

			<!-- // START : 댓글 등록 영역  -->
			<div class="panel panel-default">
				<div class="panel-body form-horizontal">
					<form name="frm_reply"
						action="<c:url value='/reply/replyRegist' />" method="post"
						onclick="return false;">
						<input type="hidden" name="reParentNo" value="${freeBoard.boNo}">
						<input type="hidden" name="reIp"
							value="${pageContext.request.remoteAddr}"> <input
							type="hidden" name="reCategory" value="FREE"> <input
							type="hidden" name="reMemId" value="${USER_INFO.userId }">

						<div class="form-group">
							<label class="col-sm-2  control-label">댓글</label>
							<div class="col-sm-8">
								<textarea rows="3" name="reContent" class="form-control">${ RECONTENT} </textarea>
							</div>
							<div class="col-sm-2">
								<button id="btn_reply_regist" type="button"
									class="btn btn-sm btn-info">등록</button>
							</div>
						</div>
					</form>
				</div>
			</div>
			<!-- // END : 댓글 등록 영역  -->


			<!-- // START : 댓글 목록 영역  -->
			<div id="id_reply_list_area">
				<div class="row">
					<div class="col-sm-2 text-right">홍길동</div>
					<div class="col-sm-6">
						<pre>내용</pre>
					</div>
					<div class="col-sm-2">12/30 23:45</div>
					<div class="col-sm-2">
						<button name="btn_reply_edit" type="button"
							class=" btn btn-sm btn-info">수정</button>
						<button name="btn_reply_delete" type="button"
							class="btn btn-sm btn-danger">삭제</button>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2 text-right">그댄 먼곳만 보네요</div>
					<div class="col-sm-6">
						<pre> 롤링롤링롤링롤링</pre>
					</div>
					<div class="col-sm-2">11/25 12:45</div>
					<div class="col-sm-2"></div>
				</div>
			</div>

			<div class="row text-center" id="id_reply_list_more">
				<a id="btn_reply_list_more"
					class="btn btn-sm btn-default col-sm-10 col-sm-offset-1"> <span
					class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
					더보기
				</a>
			</div>
			<!-- // END : 댓글 목록 영역  -->


			<!-- START : 댓글 수정용 Modal -->
			<div class="modal fade" id="id_reply_edit_modal" role="dialog">
				<div class="modal-dialog">
					<!-- Modal content-->
					<div class="modal-content">
						<form name="" action="<c:url value='/reply/replyModify' />"
							method="post" onclick="return false;">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">×</button>
								<h4 class="modal-title">댓글수정</h4>
							</div>
							<div class="modal-body">
								<input type="hidden" name="reNo" value="">
								<textarea rows="3" name="reContent" class="form-control"></textarea>
								<input type="hidden" name="reMemId" value="${USER_INFO.userId }">
							</div>
							<div class="modal-footer">
								<button id="btn_reply_modify" type="button"
									class="btn btn-sm btn-info">저장</button>
								<button type="button" class="btn btn-default btn-sm"
									data-dismiss="modal">닫기</button>
							</div>
						</form>
					</div>
				</div>
			</div>
			<!-- END : 댓글 수정용 Modal -->
		</div>
		<!-- reply container -->
	</c:if>
</body>
<script type="text/javascript">
var params  = {"curPage":1, "rowSizePerPage":10, "reCategory" : "FREE", "reParentNo": ${freeBoard.boNo}};	
function fn_reply_list(){
	//아작스 호출해서 DB에 있는 reply 데이터 가지고 온다.
	//가지고 오면 (success)하면 댓글 div 만들어준다.
	//list를 가지고 오니까jquery 반복문 써서 div 여러개 만들어주면 된다.
	$.ajax({
		url : "<c:url value='/reply/replyList'/>"
		,type : 'POST'
		,data : params 
		,dataType : 'JSON' // 받을 때 data를 어떻게 받을지
		,success : function(data){
			console.log(data); 
			$.each(data.data, function (index, element) {
				var str = "";
				str = str+ '<div class="row" data-re-no ="'+element.reNo+' ">'
				+'<div class="col-sm-2 text-right">'+element.reMemName+'</div>'
				+'<div class="col-sm-6">'
				+'<pre>'+element.reContent +'</pre> </div>'
				+'<div class="col-sm-2">'+element.reRegDate+'</div>'
				+'<div class="col-sm-2">'
				//브라우저에서는 ""안에 값 넣어줘야함. 그냥 ${USER_INFO.userId}면 출력만 됨
				if(element.reMemId == "${USER_INFO.userId}"){
				str = 
				str 
				+'<button name="btn_reply_edit" type="button" class=" btn btn-sm btn-info">수정</button>'
				+'<button name="btn_reply_delete" type="button" class="btn btn-sm btn-danger">삭제</button>'	
				}
				str  = str +'</div> </div>'
				console.log(str);
				$("#id_reply_list_area").append(str);
			})
				params.curPage+=1;
			
		}
	});
}
	//fn_reply_list();
	$("#id_reply_list_more").on("click", function(e){
		e.preventDefault();
		fn_reply_list();
	});//더보기
	
	$("#btn_reply_regist").on("click", function(e){
		e.preventDefault();
		//등록 버튼의 상위 form 찾기
		//ajax 호출 - form태그.serialize() - 해당 form 태그 안에 있는 input, textarea, select 태그 전부 
		//파라미터로 보냄 {"reCategory" : "", "reParentNo" : ""}
		//success하면 등록 영역에 썼던 내용 지우기 .val, text, html
		//댓글 목록에 내용 싹 다 지우고 remove
		//params.curPage = 1로 바꾸고 다시 fn_reply_list() - 새로 등록된 글이 맨 위에 나옴
		var reply = $(this).closest("form").serialize();
		var $form = $(this).closest("form")
		console.log(reply);
		$.ajax({
			url : "<c:url value='/reply/replyRegist'/>"
				,type : 'POST'
				,dataType : 'JSON' // 받을 때 data를 어떻게 받을지
				,data : reply
				,success : function (data) {
					console.log(data);
					$form.find("textarea[name='reContent']").val('');
					$("#id_reply_list_area").html('');
					params.curPage = 1;
					fn_reply_list();
 				}
				,error : function (req, st, err) {
					if(req.status == 401){
					location.href = "<c:url value='/login/login.wow'/>";
					}
				}
		});//ajax
	});//등록버튼
	
	//수정버튼 function(){}은 동적으로 생긴 태그에도 적용되는 것 같다.
	//$().click()은 동적으로 생긴 태그에 적용 x
	//$().on("click")은 동적으로 생긴 태그에도 적용됨
	$("#id_reply_list_area").on("click", 'button[name = "btn_reply_edit"]', function (e){
				//현재 버튼의 상위 div(하나의 댓글 전체)을 찾아라
				//그 div에서 현재 댓글의 내용, data-re-no에 있는 값을
				//modal에 있는 <input name = reNo> 태그에 값으로 복사
				var reply = $(this).closest(".row").find("pre").text();
				var reNo = $(this).closest(".row").data("re-no");
				
				$('#id_reply_edit_modal').find('.modal-body textarea').html(reply);
				$('input[name=reNo]').attr('value',reNo);
				
				$('#id_reply_edit_modal').modal('show');
				
			});//수정버튼

			//모달창의 저장버튼
			//modify. 아작스 요청
			//파라미터로 글번호, 글내용
			//아작스 성공 후 modal 닫기 , list 삭제 후 params.curPage-1, fn_reply_list
			//선택 : modal 창에 있는 reNo, textarea 내용 지우기
			
			$("#btn_reply_modify").on("click", function (e) {
			//	var reContent = $(this).closest("form").find(".modal-body textarea").html();
			//	var reNo = $(this).closest("form").find('input[name=reNo]').val(); 
			//var params = {"reNo" :reNo , "reContent" : reContent};
				
				e.preventDefault(); // 폼태그 안에 있는 버튼임
				$form = $(this).closest('form');		
				console.log($form.serialize());
				
				$.ajax({
					url :  "<c:url value='/reply/replyModify'/>"
					,type : 'POST'
					,data : $form.serialize()
					,dataType : 'JSON'
					,success : function(data){
						$('#id_reply_edit_modal').modal('hide');
						$("#id_reply_list_area").html('');
						params.curPage = 1;
						fn_reply_list();
					}
					,error : function (req, st, err) {
						alert("실패");
					}
				});
			});//모달창 저장버튼
			
			//삭제버튼
			//파라미터는 reNo, reMemId
			//아작스 호출 후 성공에서 목록 지우고 param.curPage = 1, fn_reply_list 대신 
			//그냥 목록영역에서 해당 댓글 div 삭제
			
			$("#id_reply_list_area").on("click", 'button[name = "btn_reply_delete"]', function(e){
				$div = $(this).closest('.row');
				reNo = $div.data('re-no');
				reMemId = "${USER_INFO.userId}";
				$.ajax({
					url : "<c:url value='/reply/replyDelete'/>"
					,type : "POST"
					,data : {"reNo" : reNo, "reMemId": reMemId}
					,dataType : 'JSON'
					,success : function(){
						$div.remove();
					}
				});
			});
</script>



<script type="text/javascript">
//selector : 그냥 태그 이름, #, ., [type, name, href 등등 = '값'], 띄어쓰기는 자식
//			: eq(), first, last		

//jquery 셀렉터로 찾은 태그는 jQuery 객체, jQuery 객체에만 jQuery 함수 사용 가능 
//javascript로 찾은 태그는 javascript 객체, javascript객체에만 javascript 함수 사용 가능

//jQuery객체에서 특정 함수나 배열을 사용하면 javascript 객체로 반환하기도 한다.
//javascript 객체를 jQuery객체로 만들기 -> $(javascript객체) 

$(document).ready(function (){
	
		event.preventDefault(); //form태그  기본 이벤트 막기
		//버튼을 기준으로 상위 form 태그 찾은 다음에, form태그 자식, 자손 중에서 textarea 찾아서
		//현재 textarea의 내용 출력
		var text = $(this).closest('form').find('textarea').val();
});
	
	//수정버튼 list영역 안에 있는 이름이 btn_reply_edit인 모든 버튼에 이벤트
	$('#id_reply_list_area').on("click", "button[name = btn_reply_edit]",function(e){
		//버튼을 기준으로 상위 div 찾고 그 다음에 div 안에 pre태그 있는 내용 alert
		e.preventDefault();
		var preText = $(this).closest('.row').find('pre').html();
		//preText = $($(this).closest('.row').find('pre')).text();
		$('#id_reply_edit_modal').modal('show');
		$('#id_reply_edit_modal').find('.modal-body textarea').val(preText);
	});



</script>
</html>






