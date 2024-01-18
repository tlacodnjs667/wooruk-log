document.getElementById("saveBtn").addEventListener("click", savePost);

async function savePost() {
  const title = titleInput.value;
  const author = authorInput.value;
  const content = editor.getMarkdown();
  const id = document.getElementById("postId").value;

  if (editor.getMarkdown().length < 30) {
    return;
  }

  const body = {
    id, title, content, author
  }

  console.log(body);
  const {data} = await axios.post("/post/modify", body,
      {headers: {"Content-Type": "application/json"}});

  console.log("data=", data);
  location.href = `/post/${data}`;
}
