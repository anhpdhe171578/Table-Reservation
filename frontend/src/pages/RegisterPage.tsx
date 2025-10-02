import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

export default function RegisterPage() {
  const [form, setForm] = useState({
    fullName: "",
    userName: "",
    email: "",
    password: "",
    phoneNumber: "",
    gender: "1", // mặc định Nam
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    try {
      const response = await axios.post("http://localhost:8080/api/auth/register", form);

      if (response.status === 200 || response.status === 201) {
        alert("Đăng ký thành công! Vui lòng đăng nhập.");
        navigate("/login");
      }
    } catch (err: any) {
      setError("Đăng ký thất bại. Vui lòng thử lại!");
      console.error("Register error:", err.response?.data || err.message);
    }
  };

  return (
    <div className="flex justify-center items-center h-screen bg-gray-100">
      <form
        onSubmit={handleRegister}
        className="bg-white p-8 rounded shadow-md w-96"
      >
        <h2 className="text-2xl font-bold mb-6 text-center">Đăng ký</h2>

        {error && <p className="text-red-500 mb-4">{error}</p>}

        <div className="mb-4">
          <label className="block mb-1">Họ và tên</label>
          <input
            type="text"
            name="fullName"
            value={form.fullName}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          />
        </div>

        <div className="mb-4">
          <label className="block mb-1">Tên đăng nhập</label>
          <input
            type="text"
            name="userName"
            value={form.userName}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          />
        </div>

        <div className="mb-4">
          <label className="block mb-1">Email</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          />
        </div>

        <div className="mb-4">
          <label className="block mb-1">Mật khẩu</label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          />
        </div>

        <div className="mb-4">
          <label className="block mb-1">Số điện thoại</label>
          <input
            type="text"
            name="phoneNumber"
            value={form.phoneNumber}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
          />
        </div>

        <div className="mb-6">
          <label className="block mb-1">Giới tính</label>
          <select
            name="gender"
            value={form.gender}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
          >
            <option value="1">Nam</option>
            <option value="0">Nữ</option>
          </select>
        </div>

        <button
          type="submit"
          className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-600"
        >
          Đăng ký
        </button>

        <p className="mt-4 text-center text-sm">
          Đã có tài khoản?{" "}
          <Link to="/login" className="text-blue-500 hover:underline">
            Đăng nhập ngay
          </Link>
        </p>
      </form>
    </div>
  );
}
