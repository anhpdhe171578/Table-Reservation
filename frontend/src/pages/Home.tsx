// src/pages/Home.tsx
import { Link } from "react-router-dom";
import { useState, useEffect, useRef } from "react";

export default function Home({ isAuthenticated = false }: { isAuthenticated?: boolean }) {
  const [open, setOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  // Đóng menu khi click ra ngoài
  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* Header */}
      <header className="bg-white shadow-md p-4 flex justify-between items-center">
        {/* Logo */}
        <h1 className="text-2xl font-bold text-green-600">Foodie</h1>

        {/* Menu giữa */}
        <nav className="flex-1">
          <ul className="flex justify-center gap-6">
            <li><Link to="/">Trang chủ</Link></li>
            <li><Link to="/blog">Blog</Link></li>
            <li><Link to="/category">Danh mục</Link></li>
          </ul>
        </nav>

        {/* Dropdown User */}
        <div className="relative" ref={dropdownRef}>
          {isAuthenticated ? (
            <div className="flex items-center gap-4">
              <Link to="/cart" className="text-xl">🛒</Link>
              <button
                onClick={() => setOpen(!open)}
                className="flex items-center gap-2 px-3 py-2 hover:bg-gray-100 rounded"
              >
                👤 <span>Tài khoản</span>
              </button>
              {open && (
                <ul className="absolute right-0 mt-2 w-40 bg-white border rounded-lg shadow-lg z-50">
                  <li><Link to="/history" className="block px-4 py-2 hover:bg-gray-100">Lịch sử</Link></li>
                  <li><Link to="/profile" className="block px-4 py-2 hover:bg-gray-100">Hồ sơ</Link></li>
                  <li><button className="w-full text-left px-4 py-2 hover:bg-gray-100">Đăng xuất</button></li>
                </ul>
              )}
            </div>
          ) : (
            <div>
              <button
                onClick={() => setOpen(!open)}
                className="flex items-center gap-2 px-3 py-2 hover:bg-gray-100 rounded"
              >
                👤 <span>Tài khoản</span>
              </button>
              {open && (
                <ul className="absolute right-0 mt-2 w-40 bg-white border rounded-lg shadow-lg z-50">
                  <li><Link to="/login" className="block px-4 py-2 hover:bg-gray-100">Đăng nhập</Link></li>
                  <li><Link to="/register" className="block px-4 py-2 hover:bg-gray-100">Đăng ký</Link></li>
                </ul>
              )}
            </div>
          )}
        </div>
      </header>

      {/* Hero Banner */}
      <section
        className="relative flex items-center justify-center text-center text-white h-[80vh] bg-cover bg-center"
        style={{ backgroundImage: "url('/images/greenhouse.jpg')" }}
      >
        <div className="absolute inset-0 bg-black/40"></div>
        <div className="relative z-10 max-w-2xl">
          <p className="uppercase tracking-widest text-sm">Save up to 50%</p>
          <h2 className="text-5xl font-bold mt-4">Gift Green</h2>
          <h3 className="text-4xl italic mt-2">This Holiday</h3>
          <Link
            to="/shop"
            className="mt-6 inline-block bg-green-600 text-white px-6 py-3 rounded-full hover:bg-green-700 transition"
          >
            Discover
          </Link>
        </div>
      </section>

      {/* Content */}
      <main className="p-6 grid grid-cols-1 md:grid-cols-3 gap-6 bg-gray-50">
        <div className="bg-white shadow rounded-lg p-4">
          <h3 className="text-lg font-semibold mb-2">Món ăn nổi bật</h3>
          <p>Danh sách các món ăn...</p>
        </div>
        <div className="bg-white shadow rounded-lg p-4">
          <h3 className="text-lg font-semibold mb-2">Bài viết mới</h3>
          <p>Danh sách blog...</p>
        </div>
        <div className="bg-white shadow rounded-lg p-4">
          <h3 className="text-lg font-semibold mb-2">Danh mục</h3>
          <p>Danh sách category...</p>
        </div>
      </main>
    </div>
  );
}
