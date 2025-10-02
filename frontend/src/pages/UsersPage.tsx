import React, { useEffect, useState } from "react";
import {
  getUsers,
  createUser,
  updateUser,
  deleteUser,
  UserDTO,
} from "../api/userAPI";

const UsersPage: React.FC = () => {
  const [users, setUsers] = useState<UserDTO[]>([]);
  const [q, setQ] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<UserDTO | null>(null);

  // form fields
  const [userName, setUserName] = useState("");
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [status, setStatus] = useState("active");
  const [password, setPassword] = useState("");

  // pagination
  const [page, setPage] = useState(1);
  const perPage = 6;

  useEffect(() => {
    fetchUsers();
  }, []);

  async function fetchUsers() {
    try {
      const data = await getUsers();
      setUsers(data || []);
    } catch (err) {
      console.error(err);
      alert("Không thể lấy users từ server");
    }
  }

  function openCreate() {
    setEditing(null);
    setUserName("");
    setFullName("");
    setEmail("");
    setPhoneNumber("");
    setStatus("active");
    setPassword("");
    setShowModal(true);
  }

  function openEdit(u: UserDTO) {
    setEditing(u);
    setUserName(u.userName ?? "");
    setFullName(u.fullName ?? "");
    setEmail(u.email ?? "");
    setPhoneNumber(u.phoneNumber ?? "");
    setStatus(u.status ?? "active");
    setPassword("");
    setShowModal(true);
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    try {
      if (editing) {
        await updateUser(editing.id, {
          userName,
          fullName,
          email,
          phoneNumber,
          status,
          ...(password ? { password } : {}),
        });
      } else {
        await createUser({
          userName,
          fullName,
          email,
          phoneNumber,
          password: password || "123456", // default nếu trống
          status,
        });
      }
      await fetchUsers();
      setShowModal(false);
    } catch (err: any) {
      console.error(err);
      alert(err?.response?.data || "Lỗi khi lưu user");
    }
  }

  async function handleDelete(id: string) {
    if (!window.confirm("Bạn chắc chắn muốn xoá user này?")) return;
    await deleteUser(id);
    fetchUsers();
  }

  // search + pagination
  const filtered = users.filter((u) => {
    const keyword = q.trim().toLowerCase();
    return (
      (u.userName ?? "").toLowerCase().includes(keyword) ||
      (u.email ?? "").toLowerCase().includes(keyword) ||
      (u.fullName ?? "").toLowerCase().includes(keyword) ||
      (u.phoneNumber ?? "").toLowerCase().includes(keyword)
    );
  });

  const totalPages = Math.max(1, Math.ceil(filtered.length / perPage));
  const current = filtered.slice((page - 1) * perPage, page * perPage);

  useEffect(() => {
    if (page > totalPages) setPage(1);
  }, [totalPages, page]);

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-slate-800">User Management</h2>
        <div className="flex items-center gap-3">
          <input
            value={q}
            onChange={(e) => setQ(e.target.value)}
            placeholder="Search username, fullname, email..."
            className="border rounded px-3 py-2 w-60"
          />
          <button
            onClick={openCreate}
            className="bg-sky-600 text-white px-4 py-2 rounded shadow"
          >
            + Add user
          </button>
        </div>
      </div>

      <div className="bg-white/90 rounded-lg shadow overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-slate-50">
            <tr>
              <th className="p-3">Username</th>
              <th className="p-3">Full Name</th>
              <th className="p-3">Email</th>
              <th className="p-3">Phone</th>
              <th className="p-3">Status</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {current.map((u) => (
              <tr key={u.id} className="border-t hover:bg-slate-50">
                <td className="p-3 font-medium">{u.userName}</td>
                <td className="p-3">{u.fullName}</td>
                <td className="p-3 text-slate-600">{u.email}</td>
                <td className="p-3">{u.phoneNumber}</td>
                <td className="p-3">
                  <span
                    className={`px-2 py-1 rounded text-white ${
                      u.status === "active" ? "bg-green-500" : "bg-gray-400"
                    }`}
                  >
                    {u.status}
                  </span>
                </td>
                <td className="p-3 space-x-2">
                  <button
                    onClick={() => openEdit(u)}
                    className="px-3 py-1 rounded bg-yellow-400"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(u.id)}
                    className="px-3 py-1 rounded bg-red-600 text-white"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
            {current.length === 0 && (
              <tr>
                <td colSpan={6} className="p-6 text-center text-slate-500">
                  Không có user nào
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      <div className="flex justify-center gap-2 mt-4">
        <button
          onClick={() => setPage((p) => Math.max(1, p - 1))}
          className="px-3 py-1 border rounded disabled:opacity-50"
          disabled={page === 1}
        >
          Prev
        </button>
        {[...Array(totalPages)].map((_, i) => {
          const p = i + 1;
          return (
            <button
              key={p}
              onClick={() => setPage(p)}
              className={`px-3 py-1 border rounded ${
                p === page ? "bg-sky-600 text-white" : "bg-white"
              }`}
            >
              {p}
            </button>
          );
        })}
        <button
          onClick={() => setPage((p) => Math.min(totalPages, p + 1))}
          className="px-3 py-1 border rounded"
          disabled={page === totalPages}
        >
          Next
        </button>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div
            className="absolute inset-0 bg-black/40"
            onClick={() => setShowModal(false)}
          />
          <div className="relative bg-white rounded-lg shadow-lg w-full max-w-md p-6 z-10">
            <h3 className="text-xl font-bold mb-3">
              {editing ? "Edit user" : "Create user"}
            </h3>
            <form onSubmit={handleSubmit} className="space-y-3">
              <input
                required
                value={userName}
                onChange={(e) => setUserName(e.target.value)}
                placeholder="Username"
                className="w-full border p-2 rounded"
              />
              <input
                required
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                placeholder="Full name"
                className="w-full border p-2 rounded"
              />
              <input
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Email"
                type="email"
                className="w-full border p-2 rounded"
              />
              <input
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
                placeholder="Phone number"
                className="w-full border p-2 rounded"
              />
              <input
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password (leave blank to keep old)"
                type="password"
                className="w-full border p-2 rounded"
              />
              <select
                value={status}
                onChange={(e) => setStatus(e.target.value)}
                className="w-full border p-2 rounded"
              >
                <option value="active">Active</option>
                <option value="inactive">Inactive</option>
              </select>
              <div className="flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="px-4 py-2 rounded border"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 rounded bg-sky-600 text-white"
                >
                  Save
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default UsersPage;
