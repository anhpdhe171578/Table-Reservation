import React, { useEffect, useState } from "react";
import { getTables, createTable, updateTable, deleteTable, TableDTO } from "../api/tableAPI";

const TablesPage: React.FC = () => {
  const [tables, setTables] = useState<TableDTO[]>([]);
  const [q, setQ] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<TableDTO | null>(null);

  // form fields
  const [tableNumber, setTableNumber] = useState("");
  const [status, setStatus] = useState("Available");
  const [areaID, setAreaID] = useState<number>(1);
  const [numberOfDesk, setNumberOfDesk] = useState(1);

  // pagination
  const [page, setPage] = useState(1);
  const perPage = 6;

  useEffect(() => {
    fetchTables();
  }, []);

  async function fetchTables() {
    try {
      const data = await getTables();
      setTables(data || []);
    } catch (err) {
      console.error(err);
      alert("Không thể lấy tables từ server");
    }
  }

  function openCreate() {
    setEditing(null);
    setTableNumber("");
    setStatus("Available");
    setAreaID(1);
    setNumberOfDesk(1);
    setShowModal(true);
  }

  function openEdit(t: TableDTO) {
    setEditing(t);
    setTableNumber(t.tableNumber);
    setStatus(t.status);
    setAreaID(t.areaID);
    setNumberOfDesk(t.numberOfDesk);
    setShowModal(true);
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    try {
      if (editing) {
        await updateTable(editing.id, { tableNumber, status, areaID, numberOfDesk });
      } else {
        await createTable({ tableNumber, status, areaID, numberOfDesk });
      }
      await fetchTables();
      setShowModal(false);
    } catch (err: any) {
      console.error(err);
      alert(err?.response?.data || "Lỗi khi lưu table");
    }
  }

  async function handleDelete(id: number) {
    if (!window.confirm("Bạn chắc chắn muốn xoá bàn này?")) return;
    await deleteTable(id);
    fetchTables();
  }

  // search + pagination
  const filtered = tables.filter((t) => {
    const keyword = q.trim().toLowerCase();
    return (
      (t?.tableNumber ?? "").toLowerCase().includes(keyword) ||
      (t?.status ?? "").toLowerCase().includes(keyword)
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
        <h2 className="text-2xl font-bold text-slate-800">Table Management</h2>
        <div className="flex items-center gap-3">
          <input
            value={q}
            onChange={(e) => setQ(e.target.value)}
            placeholder="Search table number or status..."
            className="border rounded px-3 py-2 w-60"
          />
          <button onClick={openCreate} className="bg-sky-600 text-white px-4 py-2 rounded shadow">
            + Add table
          </button>
        </div>
      </div>

      <div className="bg-white/90 rounded-lg shadow overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-slate-50">
            <tr>
              <th className="p-3">Table Number</th>
              <th className="p-3">Status</th>
              <th className="p-3">AreaID</th>
              <th className="p-3">Number of Desk</th>
              <th className="p-3 w-48">Actions</th>
            </tr>
          </thead>
          <tbody>
            {current.map((t) => (
              <tr key={t.id} className="border-t hover:bg-slate-50">
                <td className="p-3 font-medium">{t.tableNumber}</td>
                <td className="p-3 text-slate-600">{t.status}</td>
                <td className="p-3">{t.areaID}</td>
                <td className="p-3">{t.numberOfDesk}</td>
                <td className="p-3 space-x-2">
                  <button onClick={() => openEdit(t)} className="px-3 py-1 rounded bg-yellow-400">
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(t.id)}
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
                  Không có table nào
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
            <h3 className="text-xl font-bold mb-3">{editing ? "Edit table" : "Create table"}</h3>
            <form onSubmit={handleSubmit} className="space-y-3">
              <input
                required
                value={tableNumber}
                onChange={(e) => setTableNumber(e.target.value)}
                placeholder="Table Number"
                className="w-full border p-2 rounded"
              />
              <select
                value={status}
                onChange={(e) => setStatus(e.target.value)}
                className="w-full border p-2 rounded"
              >
                <option value="Available">Available</option>
                <option value="Occupied">Occupied</option>
                <option value="Reserved">Reserved</option>
              </select>
              <input
                type="number"
                value={areaID}
                onChange={(e) => setAreaID(Number(e.target.value))}
                placeholder="Area ID"
                className="w-full border p-2 rounded"
              />
              <input
                type="number"
                value={numberOfDesk}
                onChange={(e) => setNumberOfDesk(Number(e.target.value))}
                placeholder="Number of Desk"
                className="w-full border p-2 rounded"
              />
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

export default TablesPage;
