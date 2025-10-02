import React, { useEffect, useState } from "react";
import {
  getRestaurants,
  createRestaurant,
  updateRestaurant,
  deleteRestaurant,
  RestaurantDTO,
} from "../api/restaurantAPI";

const RestaurantsPage: React.FC = () => {
  const [restaurants, setRestaurants] = useState<RestaurantDTO[]>([]);
  const [q, setQ] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<RestaurantDTO | null>(null);

  // form fields
  const [address, setAddress] = useState("");

  // pagination
  const [page, setPage] = useState(1);
  const perPage = 6;

  useEffect(() => {
    fetchRestaurants();
  }, []);

  async function fetchRestaurants() {
    try {
      const data = await getRestaurants();
      setRestaurants(data || []);
    } catch (err) {
      console.error(err);
      alert("Không thể lấy restaurants từ server");
    }
  }

  function openCreate() {
    setEditing(null);
    setAddress("");
    setShowModal(true);
  }

  function openEdit(r: RestaurantDTO) {
    setEditing(r);
    setAddress(r.address);
    setShowModal(true);
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    try {
      if (editing) {
        await updateRestaurant(editing.id, { address });
      } else {
        await createRestaurant({ address });
      }
      await fetchRestaurants();
      setShowModal(false);
    } catch (err: any) {
      console.error("Lỗi khi lưu restaurant:", err);
      if (err?.response?.data) {
        alert(JSON.stringify(err.response.data));
      } else {
        alert("Lỗi khi lưu restaurant");
      }
    }
  }

  async function handleDelete(id: number) {
    if (!window.confirm("Bạn chắc chắn muốn xoá restaurant này?")) return;
    await deleteRestaurant(id);
    fetchRestaurants();
  }

  // search + pagination
  const filtered = restaurants.filter((r) =>
    (r?.address ?? "").toLowerCase().includes(q.trim().toLowerCase())
  );

  const totalPages = Math.max(1, Math.ceil(filtered.length / perPage));
  const current = filtered.slice((page - 1) * perPage, page * perPage);

  useEffect(() => {
    if (page > totalPages) setPage(1);
  }, [totalPages]);

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-slate-800">Restaurant Management</h2>
        <div className="flex items-center gap-3">
          <input
            value={q}
            onChange={(e) => setQ(e.target.value)}
            placeholder="Search by address..."
            className="border rounded px-3 py-2 w-60"
          />
          <button
            onClick={openCreate}
            className="bg-sky-600 text-white px-4 py-2 rounded shadow"
          >
            + Add restaurant
          </button>
        </div>
      </div>

      {/* Table */}
      <div className="bg-white/90 rounded-lg shadow overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-slate-50">
            <tr>
              <th className="p-3">ID</th>
              <th className="p-3">Address</th>
              <th className="p-3">Created At</th>
              <th className="p-3">Updated At</th>
              <th className="p-3 w-48">Actions</th>
            </tr>
          </thead>
          <tbody>
            {current.map((r) => (
              <tr key={r.id} className="border-t hover:bg-slate-50">
                <td className="p-3">{r.id}</td>
                <td className="p-3 font-medium">{r.address}</td>
                <td className="p-3">{r.createdAt ? new Date(r.createdAt).toLocaleString() : "-"}</td>
                <td className="p-3">{r.updatedAt ? new Date(r.updatedAt).toLocaleString() : "-"}</td>
                <td className="p-3 space-x-2">
                  <button
                    onClick={() => openEdit(r)}
                    className="px-3 py-1 rounded bg-yellow-400"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(r.id)}
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
                  Không có restaurant nào
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
              {editing ? "Edit restaurant" : "Create restaurant"}
            </h3>
            <form onSubmit={handleSubmit} className="space-y-3">
              <input
                required
                value={address}
                onChange={(e) => setAddress(e.target.value)}
                placeholder="Restaurant Address"
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

export default RestaurantsPage;
