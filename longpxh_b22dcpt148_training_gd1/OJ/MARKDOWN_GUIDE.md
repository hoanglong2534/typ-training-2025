# Cách sử dụng Markdown để render đề bài

## 📖 Tổng quan

Dự án đã được cấu hình để sử dụng **Markdown** cho việc hiển thị đề bài. Điều này cho phép:
- ✅ Hiển thị code blocks với format đẹp
- ✅ Inline code với background màu xám
- ✅ Bold, italic, lists, etc.
- ✅ An toàn, không có XSS

## 🛠️ Đã cài đặt

```bash
npm install react-markdown remark-gfm --legacy-peer-deps
```

- **react-markdown**: Thư viện render markdown
- **remark-gfm**: Hỗ trợ GitHub Flavored Markdown (tables, strikethrough, etc.)

## 💡 Cách viết Markdown

### 1. Text thường
```markdown
Đây là text bình thường
```

### 2. Bold text
```markdown
**Text in đậm** hoặc __Text in đậm__
```

### 3. Inline code
```markdown
Sử dụng function `printf()` để in ra màn hình
```

### 4. Code blocks
````markdown
```cpp
#include <iostream>
using namespace std;

int main() {
    cout << "Hello World!" << endl;
    return 0;
}
```
````

### 5. Lists
```markdown
- Item 1
- Item 2
  - Sub item 2.1
  - Sub item 2.2
```

### 6. Numbered lists
```markdown
1. Bước đầu tiên
2. Bước thứ hai
3. Bước thứ ba
```

## 📝 Ví dụ đề bài hoàn chỉnh

```markdown
## Bài toán: Two Sum

Cho một mảng số nguyên `nums` và một số nguyên `target`. Hãy tìm **hai số** trong mảng sao cho tổng của chúng bằng `target`.

### Input
- Dòng đầu tiên: số nguyên `n` (số phần tử của mảng)
- Dòng thứ hai: `n` số nguyên cách nhau bởi dấu cách
- Dòng thứ ba: số nguyên `target`

### Output
In ra hai chỉ số của hai số có tổng bằng `target`

### Ví dụ

**Input:**
```
5
2 7 11 15 6
9
```

**Output:**
```
0 1
```

### Giải thích
- `nums[0] + nums[1] = 2 + 7 = 9`
- Nên output là `0 1`

### Code mẫu (C++)

```cpp
#include <iostream>
#include <vector>
using namespace std;

int main() {
    int n, target;
    cin >> n;
    
    vector<int> nums(n);
    for(int i = 0; i < n; i++) {
        cin >> nums[i];
    }
    cin >> target;
    
    // Your solution here
    
    return 0;
}
```

**Lưu ý:** Bạn cần xử lý trường hợp không tìm thấy cặp số nào.
```

## 🎨 Custom Styling

Code hiện tại đã có custom styling cho:

- **Code blocks**: Background xám, padding, border radius
- **Inline code**: Background xám nhạt, padding nhỏ
- **Paragraphs**: Margin bottom, line height thoáng
- **Bold text**: Font weight bold

## 📂 Cách sử dụng trong code

1. **Lưu đề bài dạng Markdown string:**
   ```tsx
   const problemDescription = `Đây là đề bài với \`code\` và **bold**`;
   ```

2. **Render với ReactMarkdown:**
   ```tsx
   <ReactMarkdown remarkPlugins={[remarkGfm]}>
       {problemDescription}
   </ReactMarkdown>
   ```

3. **Trong thực tế, lấy từ API:**
   ```tsx
   const [problem, setProblem] = useState(null);
   
   useEffect(() => {
       fetch(`/api/problems/${id}`)
           .then(res => res.json())
           .then(data => setProblem(data));
   }, [id]);
   
   return (
       <ReactMarkdown>
           {problem?.description}
       </ReactMarkdown>
   );
   ```

## 🔧 Các ký tự đặc biệt được hỗ trợ

- `<`, `>`, `&` - Tự động escape
- Code: `cout << "Hello"` sẽ hiển thị đúng
- Math operators: `+`, `-`, `*`, `/`, `%`
- Special chars: `@`, `#`, `$`, etc.

## ⚠️ Lưu ý

- Không cần escape HTML trong markdown string
- GitHub Flavored Markdown hỗ trợ tables, strikethrough
- Syntax highlighting sẽ cần thêm thư viện nếu muốn màu sắc cho code
