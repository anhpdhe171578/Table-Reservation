// src/pages/BlogsPage.tsx
import React, { useEffect, useState } from "react";
import { getBlogs, createBlog, updateBlog, deleteBlog, BlogDTO } from "../api/blogAPI";

const BlogsPage: React.FC = () => {
  const [blogs, setBlogs] = useState<BlogDTO[]>([]);
  const [q, setQ] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<BlogDTO | null>(null);

  // form fields
  const [title, setTitle] = useState("");
  const [subTitle, setSubTitle] = useState("");
  const [description, setDescription] = useState("");
  const [image, setImage] = useState("");
  const [createAtBy, setCreateAtBy] = useState("");
  const [status, setStatus] = useState("Draft");

  // pagination
  const [page, setPage] = useState(1);
  const perPage = 6;

  useEffect(() => {
    fetchBlogs();
  }, []);

  async function fetchBlogs() {
    try {
      const data = await getBlogs();
      setBlogs(data || []);
    } catch (err) {
      console.error(err);
      alert("Không thể lấy blogs từ server");
    }
  }

  function openCreate() {
    setEditing(null);
    setTitle("");
    setSubTitle("");
    setDescription("");
    setImage("");
    setCreateAtBy("");
    setStatus("Draft");
    setShowModal(true);
  }

  function openEdit(b: BlogDTO) {
    setEditing(b);
    setTitle(b.title);
    setSubTitle(b.subTitle);
    setDescription(b.description);
    setImage(b.image);
    setCreateAtBy(b.createAtBy);
    setStatus(b.status);
    setShowModal(true);
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    try {
      if (editing) {
        await updateBlog(editing.id, { title, subTitle, description, image, createAtBy, status });
      } else {
        await createBlog({ title, subTitle, description, image, createAtBy, status });
      }
      await fetchBlogs();
      setShowModal(false);
    } catch (err: any) {
      console.error(err);
      alert(err?.response?.data || "Lỗi khi lưu blog");
    }
  }

  async function handleDelete(id: number) {
    if (!window.confirm("Bạn chắc chắn muốn xoá blog này?")) return;
    await deleteBlog(id);
    fetchBlogs();
  }

  // search + pagination
  const filtered = blogs.filter((b) => {
    const keyword = q.trim().toLowerCase();
    return (
      (b?.title ?? "").toLowerCase().includes(keyword) ||
      (b?.createAtBy ?? "").toLowerCase().includes(keyword) ||
      (b?.status ?? "").toLowerCase().includes(keyword)
    );
  });

  const totalPages = Math.max(1, Math.ceil(filtered.length / perPage));
  const current = filtered.slice((page - 1) * perPage, page * perPage);

  useEffect(() => {
    if (page > totalPages) setPage(1);
  }, [totalPages]);

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-slate-800">Blog Management</h2>
        <div className="flex items-center gap-3">
          <input
            value={q}
            onChange={(e) => setQ(e.target.value)}
            placeholder="Search title, author, status..."
            className="border rounded px-3 py-2 w-60"
          />
          <button onClick={openCreate} className="bg-sky-600 text-white px-4 py-2 rounded shadow">
            + Add blog
          </button>
        </div>
      </div>

      <div className="bg-white/90 rounded-lg shadow overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-slate-50">
            <tr>
              <th className="p-3">Title</th>
              <th className="p-3">SubTitle</th>
              <th className="p-3">Author</th>
              <th className="p-3">Status</th>
              <th className="p-3 w-48">Actions</th>
            </tr>
          </thead>
          <tbody>
            {current.map((b) => (
              <tr key={b.id} className="border-t hover:bg-slate-50">
                <td className="p-3 font-medium">{b.title}</td>
                <td className="p-3">{b.subTitle}</td>
                <td className="p-3">{b.createAtBy}</td>
                <td className="p-3">{b.status}</td>
                <td className="p-3 space-x-2">
                  <button onClick={() => openEdit(b)} className="px-3 py-1 rounded bg-yellow-400">
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(b.id)}
                    className="px-3 py-1 rounded bg-red-600 text-white"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
            {current.length === 0 && (
              <tr>
                <td colSpan={5} className="p-6 text-center text-slate-500">
                  Không có blog nào
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
              className={`px-3 py-1 border rounded ${p === page ? "bg-sky-600 text-white" : "bg-white"}`}
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
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowModal(false)} />
          <div className="relative bg-white rounded-lg shadow-lg w-full max-w-md p-6 z-10">
            <h3 className="text-xl font-bold mb-3">{editing ? "Edit blog" : "Create blog"}</h3>
            <form onSubmit={handleSubmit} className="space-y-3">
              <input
                required
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Title"
                className="w-full border p-2 rounded"
              />
              <input
                value={subTitle}
                onChange={(e) => setSubTitle(e.target.value)}
                placeholder="SubTitle"
                className="w-full border p-2 rounded"
              />
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Description"
                className="w-full border p-2 rounded"
                rows={3}
              />
              <input
                value={image}
                onChange={(e) => setImage(e.target.value)}
                placeholder="Image URL"
                className="w-full border p-2 rounded"
              />
              <input
                value={createAtBy}
                onChange={(e) => setCreateAtBy(e.target.value)}
                placeholder="Author"
                className="w-full border p-2 rounded"
              />
              <select
                value={status}
                onChange={(e) => setStatus(e.target.value)}
                className="w-full border p-2 rounded"
              >
                <option value="Draft">Draft</option>
                <option value="Published">Published</option>
                <option value="Hidden">Hidden</option>
              </select>
              <div className="flex justify-end gap-2">
                <button type="button" onClick={() => setShowModal(false)} className="px-4 py-2 rounded border">
                  Cancel
                </button>
                <button type="submit" className="px-4 py-2 rounded bg-sky-600 text-white">
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

export default BlogsPage;
