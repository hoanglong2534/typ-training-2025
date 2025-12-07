# Hướng dẫn sử dụng Proxy trong Next.js

## 📖 Proxy là gì?

**Proxy** = Trung gian giữa Frontend và Backend

```
Browser → Next.js (localhost:3000) → Proxy → Spring Boot (localhost:8080)
```

## ✅ Đã setup gì?

File `next.config.ts` đã được config:

```typescript
async rewrites() {
  return [
    {
      source: '/api/:path*',
      destination: 'http://localhost:8080/api/:path*',
    },
  ];
}
```

**Giải thích:**
- `source: '/api/:path*'` - Khi bạn gọi `/api/problems`, `/api/users`, v.v...
- `destination: 'http://localhost:8080/api/:path*'` - Next.js sẽ tự động forward đến backend
- `:path*` - Match tất cả đường dẫn con

## 🚀 Cách sử dụng

### **TRƯỚC ĐÂY (không dùng proxy):**

```tsx
// ❌ Gọi trực tiếp backend → CORS error!
const response = await fetch('http://localhost:8080/api/problems');
```

### **BÂY GIỜ (dùng proxy):**

```tsx
// ✅ Gọi qua Next.js → Auto forward → No CORS!
const response = await fetch('/api/problems');
```

## 📝 Ví dụ thực tế

### **1. Lấy danh sách problems:**

```tsx
'use client';
import { useEffect, useState } from 'react';

export default function ProblemsPage() {
    const [problems, setProblems] = useState([]);

    useEffect(() => {
        // Gọi /api/problems → tự động forward đến http://localhost:8080/api/problems
        fetch('/api/problems')
            .then(res => res.json())
            .then(data => setProblems(data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div>
            {problems.map(p => (
                <div key={p.id}>{p.title}</div>
            ))}
        </div>
    );
}
```

### **2. Lấy chi tiết 1 problem:**

```tsx
const id = 1;
const response = await fetch(`/api/problems/${id}`);
const problem = await response.json();

// Thực tế gọi: http://localhost:8080/api/problems/1
```

### **3. POST request (submit bài):**

```tsx
const submitSolution = async (code: string) => {
    const response = await fetch('/api/submissions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            problemId: 1,
            code: code,
            language: 'cpp'
        })
    });
    
    const result = await response.json();
    return result;
};
```

## 🔧 Flow hoạt động chi tiết

```
1. Browser gọi: fetch('/api/problems')
   ↓
2. Next.js nhận request tại: localhost:3000/api/problems
   ↓
3. Next.js check rewrites config
   ↓
4. Match được rule: source: '/api/:path*'
   ↓
5. Forward request đến: http://localhost:8080/api/problems
   ↓
6. Spring Boot xử lý và trả response
   ↓
7. Next.js forward response về Browser
   ↓
8. Browser nhận được data
```

## ⚙️ Customization

### **Thay đổi backend URL:**

Edit `next.config.ts`:

```typescript
async rewrites() {
  return [
    {
      source: '/api/:path*',
      destination: 'http://your-backend-url:8080/api/:path*',
    },
  ];
}
```

### **Thêm nhiều backend:**

```typescript
async rewrites() {
  return [
    {
      source: '/api/v1/:path*',
      destination: 'http://localhost:8080/api/:path*',
    },
    {
      source: '/api/v2/:path*',
      destination: 'http://localhost:9090/api/:path*',
    },
  ];
}
```

### **Thêm headers, authentication:**

Nếu cần thêm auth token, dùng middleware hoặc API routes:

```typescript
// app/api/problems/route.ts
export async function GET() {
    const response = await fetch('http://localhost:8080/api/problems', {
        headers: {
            'Authorization': 'Bearer token...',
        }
    });
    const data = await response.json();
    return Response.json(data);
}
```

## 🐛 Troubleshooting

### **1. Proxy không hoạt động:**

- ✅ Restart dev server: `npm run dev`
- ✅ Check backend đang chạy: `http://localhost:8080/api/...`
- ✅ Check console có lỗi gì không

### **2. CORS error vẫn xảy ra:**

- Proxy chỉ hoạt động trong development (`npm run dev`)
- Trong production cần config CORS ở backend

### **3. 404 Not Found:**

- Check backend route có đúng không
- Verify: `curl http://localhost:8080/api/problems`

## 📌 Lưu ý quan trọng

1. **Development only:** Rewrites hoạt động tốt nhất trong dev mode
2. **Production:** Cần setup reverse proxy (nginx, Apache) hoặc deploy cùng server
3. **Restart required:** Mỗi lần thay đổi `next.config.ts` phải restart server
4. **Backend must be running:** Spring Boot phải chạy ở port 8080

## 🎯 Tóm tắt

- ✅ Đã config proxy trong `next.config.ts`
- ✅ Gọi API đơn giản: `fetch('/api/...')`
- ✅ Không cần lo CORS
- ✅ Backend URL được ẩn khỏi client
- ✅ Dễ thay đổi backend URL khi deploy

**Bắt đầu dùng ngay:**
```tsx
const data = await fetch('/api/problems').then(r => r.json());
```
