// src/pages/DishesPage.tsx
import React, { useEffect, useState } from "react";
import {
  getDishes,
  createDish,
  updateDish,
  deleteDish,
  DishDTO,
} from "../api/dishAPI";

const DishesPage: React.FC = () => {
  const [dishes, setDishes] = useState<DishDTO[]>([]);
  const [q, setQ] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<DishDTO | null>(null);

  // form fields
  const [name, setName] = useState("");
  const [price, setPrice] = useState<number>(0);
  const [description, setDescription] = useState("");
  const [image, setImage] = useState(""); // lưu base64 string
  const [previewImage, setPreviewImage] = useState<string | null>(null);
  const [type, setType] = useState("Food");
  const [categoryId, setCategoryId] = useState<number>(1);

  // pagination
  const [page, setPage] = useState(1);
  const perPage = 6;

  useEffect(() => {
    fetchDishes();
  }, []);

  async function fetchDishes() {
    try {
      const data = await getDishes();
      setDishes(data || []);
    } catch (err) {
      console.error(err);
      alert("Không thể lấy dishes từ server");
    }
  }

  function openCreate() {
    setEditing(null);
    setName("");
    setPrice(0);
    setDescription("");
    setImage("");
    setPreviewImage(null);
    setType("Food");
    setCategoryId(1);
    setShowModal(true);
  }

  function openEdit(d: DishDTO) {
    setEditing(d);
    setName(d.name);
    setPrice(d.price);
    setDescription(d.description);
    setImage(d.image);
    setPreviewImage(d.image || null);
    setType(d.type);
    setCategoryId(d.categoryId);
    setShowModal(true);
  }

  // chọn file ảnh -> convert base64
  function handleImageChange(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewImage(reader.result as string);
        setImage(reader.result as string); // base64 string
      };
      reader.readAsDataURL(file);
    }
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    try {
      if (editing) {
        await updateDish(editing.id, {
          name,
          price,
          description,
          image,
          type,
          categoryId,
        });
      } else {
        await createDish({
          name,
          price,
          description,
          image,
          type,
          categoryId,
        });
      }
      await fetchDishes();
      setShowModal(false);
    } catch (err: any) {
      console.error("Lỗi khi lưu dish:", err);

      // Trường hợp BE trả object → chuyển sang string JSON
      if (err?.response?.data) {
        alert(JSON.stringify(err.response.data));
      } else {
        alert("Lỗi khi lưu dish");
      }
    }
  }


  async function handleDelete(id: number) {
    if (!window.confirm("Bạn chắc chắn muốn xoá món này?")) return;
    await deleteDish(id);
    fetchDishes();
  }

  // search + pagination
  const filtered = dishes.filter((d) => {
    const keyword = q.trim().toLowerCase();
    return (
      (d?.name ?? "").toLowerCase().includes(keyword) ||
      (d?.type ?? "").toLowerCase().includes(keyword)
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
        <h2 className="text-2xl font-bold text-slate-800">Dish Management</h2>
        <div className="flex items-center gap-3">
          <input
            value={q}
            onChange={(e) => setQ(e.target.value)}
            placeholder="Search dish name or type..."
            className="border rounded px-3 py-2 w-60"
          />
          <button
            onClick={openCreate}
            className="bg-sky-600 text-white px-4 py-2 rounded shadow"
          >
            + Add dish
          </button>
        </div>
      </div>

      <div className="bg-white/90 rounded-lg shadow overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-slate-50">
            <tr>
              <th className="p-3">Image</th>
              <th className="p-3">Name</th>
              <th className="p-3">Price</th>
              <th className="p-3">Description</th>
              <th className="p-3">Type</th>
              <th className="p-3">CategoryID</th>
              <th className="p-3 w-48">Actions</th>
            </tr>
          </thead>
          <tbody>
            {current.map((d) => (
              <tr key={d.id} className="border-t hover:bg-slate-50">
                <td className="p-3">
                  {d.image ? (
                    <img
                      src={d.image.startsWith("data:image") ? d.image : `data:image/jpeg;base64,${d.image}`}
                      alt={d.name}
                      className="w-16 h-16 object-cover rounded border"
                    />
                  ) : (
                    <span className="text-slate-400 italic">No image</span>
                  )}
                </td>
                <td className="p-3 font-medium">{d.name}</td>
                <td className="p-3">{d.price}</td>
                <td className="p-3 text-slate-600">{d.description}</td>
                <td className="p-3">{d.type}</td>
                <td className="p-3">{d.categoryId}</td>
                <td className="p-3 space-x-2">
                  <button
                    onClick={() => openEdit(d)}
                    className="px-3 py-1 rounded bg-yellow-400"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(d.id)}
                    className="px-3 py-1 rounded bg-red-600 text-white"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
            {current.length === 0 && (
              <tr>
                <td colSpan={7} className="p-6 text-center text-slate-500">
                  Không có dish nào
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
              {editing ? "Edit dish" : "Create dish"}
            </h3>
            <form onSubmit={handleSubmit} className="space-y-3">
              <input
                required
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Dish Name"
                className="w-full border p-2 rounded"
              />
              <input
                type="number"
                required
                value={price}
                onChange={(e) => setPrice(Number(e.target.value))}
                placeholder="Price"
                className="w-full border p-2 rounded"
              />
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Description"
                className="w-full border p-2 rounded"
              />

              {/* File upload */}
              <div>
                <label className="block text-sm font-medium mb-1">
                  Ảnh món ăn
                </label>
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleImageChange}
                  className="w-full border p-2 rounded"
                />
                {previewImage && (
                  <img
                    src={previewImage}
                    alt="Preview"
                    className="mt-2 w-40 h-40 object-cover rounded border"
                  />
                )}
              </div>

              <select
                value={type}
                onChange={(e) => setType(e.target.value)}
                className="w-full border p-2 rounded"
              >
                <option value="Food">Food</option>
                <option value="Drink">Drink</option>
                <option value="Other">Other</option>
              </select>
              <input
                type="number"
                value={categoryId}
                onChange={(e) => setCategoryId(Number(e.target.value))}
                placeholder="Category ID"
                className="w-full border p-2 rounded"
              />
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

export default DishesPage;
