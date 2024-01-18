const Editor = toastui.Editor;

const editor = new Editor({
  el: document.querySelector('#editor'),
  name: "content",
  height: '600px',
  initialEditType: 'markdown',
  previewStyle: 'vertical',
  initialValue: document.querySelector("#contentHiddenInput").value ?? '',
  hooks: {
    async addImageBlobHook(blob, callback) {  // 이미지 업로드 로직 커스텀
      try {

        /*
         * 1. 에디터에 업로드한 이미지를 FormData 객체에 저장
         *    (이때, 컨트롤러 uploadEditorImage 메서드의 파라미터인 'image'와 formData에 append 하는 key('image')값은 동일해야 함)
         */
        const formData = new FormData();
        formData.append('file', blob);

        // 2. FileApiController - uploadEditorImage 메서드 호출
        const {data} = await axios.post("/image", formData, {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        });

        // 3. 컨트롤러에서 전달받은 디스크에 저장된 파일명
        console.dir(data);

        // 4. addImageBlobHook의 callback 함수를 통해, 디스크에 저장된 이미지를 에디터에 렌더링

        const imageUrl = `/image?imgUrl=${data}`;
        callback(imageUrl, 'image alt attribute');
        console.log(editor.value);
      } catch (error) {
        console.error('업로드 실패 : ', error);
      }
    }
  }
});

const titleInput = document.querySelector("#titleInput");
const authorInput = document.querySelector("#authorInput");

