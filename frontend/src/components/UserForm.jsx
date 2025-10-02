import { useState, useEffect } from "react";
import axiosClient from "../api/axiosClient";

export default function UserForm({ fetchUsers, editingUser, setEditingUser }) {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");

  useEffect(() => {
    if (editingUser) {
      setUsername(editingUser.username);
      setEmail(editingUser.email);
    } else {
      setUsername("");
      setEmail("");
    }
  }, [editingUser]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (editingUser) {
      await axiosClient.put(`/users/${editingUser.id}`, { username, email });
    } else {
      // nếu cần đăng ký user mới → /api/auth/register
      await axiosClient.post("/auth/register", { username, email, password: "123456" });
    }
    fetchUsers();
    setEditingUser(null);
  };

  return (
    <form onSubmit={handleSubmit} className="flex gap-4 mb-4">
      <input
        type="text"
        placeholder="Username"
        className="p-2 border rounded"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="email"
        placeholder="Email"
        className="p-2 border rounded"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <button className="bg-green-500 text-white px-4 rounded">
        {editingUser ? "Update" : "Add"}
      </button>
      {editingUser && (
        <button
          type="button"
          className="bg-gray-400 text-white px-4 rounded"
          onClick={() => setEditingUser(null)}
        >
          Cancel
        </button>
      )}
    </form>
  );
}
