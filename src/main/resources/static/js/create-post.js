document.getElementById("saveBtn").addEventListener("click", savePost);

async function savePost() {

  console.log("게시글 저장하기")

  const title = titleInput.value;
  const author = authorInput.value;
  const content = editor.getMarkdown();

  if (editor.getMarkdown().length < 30) {
    return;
  }

  const body = {
    title, content, author
  }

  console.log(body);
  const {data} = await axios.post("/post/create", body,
      {headers: {"Content-Type": "application/json"}});

  console.log("data=", data);
  location.href = `/post/${data}`;
}
