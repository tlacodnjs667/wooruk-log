document.addEventListener("DOMContentLoaded", () => {
  let firstClickShowCmt = true;
  let curPage, totalPage;

  // 댓글 리스트
  const commentBox = document.querySelector("div#comment-box");

  const postId = document.getElementById("postIdInput").value;
  // 댓글 개수 div
  const commentCountEl = document.querySelector("#comment-count");
  axios.get(
      `/api/comments/${postId}?curPage=${curPage ?? 0}`).then(res => {
        commentCountEl.innerHTML = res.data.totalComments;
  });


  window.addEventListener("scroll", () => {

    if (curPage >= totalPage || !curPage) {
      return;
    }

    console.log("스크롤 이벤트");
    const scrollHeight = document.documentElement.scrollHeight;
    const scrollTop = document.documentElement.scrollTop;
    const clientHeight = document.documentElement.clientHeight;

    if (scrollTop + clientHeight >= scrollHeight) {
      loadComment();
    }
  })

  //loadComment();

  const btnRegisterComment = document.querySelector("#register-comment");
  btnRegisterComment.addEventListener("click", registerComment)

  // 등록할 댓글 작성란
  const text = document.querySelector("textarea#cmtText");

  const updateBtnInModal = document.querySelector("#update-btn");
  const ctextToModify = document.querySelector("#ctext-to-modify");

  ctextToModify.addEventListener("keyup", checkCmtValueLen);

  function clickUpdateCmt(e) {
    const cmtId = e.target.getAttribute("cmt-id");

    const commentItem = document.querySelector(`#comment-item-${cmtId}`);
    const content = commentItem.querySelector(".content");

    ctextToModify.value = content.value;
    document.querySelector(
        "#ctext-to-modify-length").innerHTML = content.value.length;

    document.getElementById("commentId").value = cmtId;
    handleResizeHeight(ctextToModify);

  }

  function resetCmtDiv() {
    curPage = 0;
    commentBox.innerHTML = "";

    loadComment();
  }

  async function registerComment() {
    console.log("댓글 등록 이벤트")
    if (text.length == 0) {
      alert("내용을 입력해주세요.");
      return;
    }

    if (text.length <= 5) {
      alert("최소 6자 이상 입력해주세요.")
      return;
    }

    //   Ajax 요청에 포함시켜서 보낼 데이터
    const data = {postId, text: text.value, writer: "익명이"};

    if (!text.value || !postId) {
      alert("댓글 내용을 입력하세요!");
      return;
    }

    try {
      const response = await axios.post("../api/comments", data);

      text.value = "";

      if (response.status === 200) {
        resetCmtDiv();
        checkCmtValueLen({target: text});

        commentCountEl.innerHTML = (parseInt(commentCountEl.innerHTML.trim()))
            + 1;
      } else {
        alert("댓글 생성에 실패했습니다.");
      }

    } catch (err) {
      console.error(err);
    }

  }

  async function loadComment() {
    let commentHtml = "";

    const {data} = await axios.get(
        `/api/comments/${postId}?curPage=${curPage ?? 0}`);

    const {totalComments} = data;
    commentCountEl.innerHTML = totalComments;

    if (!curPage && !totalComments) {
      commentHtml = `
      <div class="card-body py-5">
        <div class="row fw-semibold text-center"
             style="font-size: 20px;">
          <div class="col my-5">
            <p style="font-size: 50px">📝</p>
            <div class="display-6">댓글이 없습니다.</div>
            <div>첫 댓글을 남겨주세요!</div>
        </div>
      </div>
    </div>`;
    } else if (curPage >= totalPage) {
      return;
    } else if (data.totalComments > 0) {
      commentHtml = data.list.map(el => {
        const {id: commentId, text, writer, createdTime} = el;

        return `<div id=${"comment-item-"
        + commentId} class="comment-item my-3">
         <div class="d-flex justify-content-between" >
              <div class="comment-info">
                <div class="writer">${writer}</div>
                <div class="text-secondary">${createdTime}</div>
              </div>
               <div class="d-flex justify-content-between mb-3" style="width: 100px">
                  <button class="update-cmt-btn btn btn-sm"  cmt-id=${commentId} data-bs-toggle="modal" data-bs-target="#staticBackdrop">수정</button>  
                  <button class="delete-cmt-btn btn btn-sm" cmt-id=${commentId}>삭제</button>  
             </div>
          </div>
          <textarea class="mt-4 mb-3 content form-control-plaintext" rows=1 style="height: auto" readonly>${text}</textarea>
      </div>
      `
      }).join("\n");

      curPage = data.curPage + 1;
    }

    commentBox.innerHTML += commentHtml;

    await document.querySelectorAll(".update-cmt-btn").forEach(
        el => {
          el.removeEventListener("click", clickUpdateCmt);
          el.addEventListener("click", clickUpdateCmt);
        }
    )

    document.querySelectorAll(".delete-cmt-btn").forEach(
        el => el.addEventListener("click", deleteComment, {once: true})
    )

    await document.querySelectorAll("textarea").forEach(el => {
      handleResizeHeight(el);
    });
  }

  text.addEventListener("keyup", checkCmtValueLen);
  document.querySelectorAll("textarea").forEach(
      el => el.addEventListener("keyup", handleResizeHeightByEvent)
  )

  function checkCmtValueLen(e) {
    let cmtInputLen = e.target.value.length;

    if (cmtInputLen >= 1000) {
      e.target.value = e.target.value.slice(0, 1000);
      cmtInputLen = 1000;
    }

    document.querySelector(
        `#${e.target.id}-length`).innerHTML = cmtInputLen;
  }

  function handleResizeHeightByEvent(e) {
    e.target.style.height = 'auto'; //height 초기화
    e.target.style.height = e.target.scrollHeight + 'px';
  };

  function handleResizeHeight(el) {
    el.style.height = 'auto'; //height 초기화
    el.style.height = el.scrollHeight + 'px';
  };

  updateBtnInModal.addEventListener("click", updateComment);

  const myModal = new bootstrap.Modal('#staticBackdrop', {
    keyboard: false
  })

  async function updateComment() {
    const commentId = document.querySelector("#commentId").value;
    const text = ctextToModify.value;

    if (text.length == 0) {
      alert("내용을 입력해주세요.")
      return;
    }

    if (text.length <= 5) {
      alert("최소 6자 이상 입력해주세요.")
      return;
    }

    const reqBody = {
      commentId, text
    };
    const {data, status} = await axios.put(`../api/comments`, reqBody);

    if (status != 200) {
      alert("댓글 수정에 실패하였습니다. 다시 시도해주세요..")
    }

    myModal.hide();
    const commentItem = document.querySelector(`#comment-item-${commentId}`);
    const content = commentItem.querySelector(".content");
    content.innerHTML = text;
  }

  async function deleteComment(e) {

    if (!confirm("정말 삭제하시겠습니까?")) {
      return;
    }

    const cmtId = e.target.getAttribute("cmt-id");

    const {status} = await axios.delete(`../api/comments/${cmtId}`);

    if (status == 204) {
      alert("댓글이 삭제되었습니다!")
      resetCmtDiv();
    } else {
      alert("댓글 삭제에 실패하였습니다.!")
    }
  }

  document.getElementById("show-cmt-btn")
  .addEventListener("click", () => {
    if (firstClickShowCmt) {
      firstClickShowCmt = !firstClickShowCmt;
      resetCmtDiv()
    } else {
      firstClickShowCmt = !firstClickShowCmt;
    }
  });
})


