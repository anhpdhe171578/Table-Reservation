import React, { useEffect, useState } from "react";
import { getOrders, deleteOrder, OrderDTO } from "../api/orderAPI";

const OrdersPage: React.FC = () => {
  const [orders, setOrders] = useState<OrderDTO[]>([]);
  const [q, setQ] = useState("");
  const [page, setPage] = useState(1);
  const [showModal, setShowModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState<OrderDTO | null>(null);
  const perPage = 6;

  useEffect(() => {
    fetchOrders();
  }, []);

  async function fetchOrders() {
    try {
      const data = await getOrders();
      setOrders(data || []);
    } catch (err) {
      console.error(err);
      alert("Không thể lấy orders từ server");
    }
  }

  function openModal(order: OrderDTO) {
    setSelectedOrder(order);
    setShowModal(true);
  }

  async function handleDelete(id: number) {
    if (!window.confirm("Bạn chắc chắn muốn xoá order này?")) return;
    await deleteOrder(id);
    fetchOrders();
  }

  const filtered = orders.filter((o) => {
    const keyword = q.trim().toLowerCase();
    return (
      (o.status ?? "").toLowerCase().includes(keyword) ||
      (o.tableID?.toString() ?? "").includes(keyword)
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
        <h2 className="text-2xl font-bold text-slate-800">Order Management</h2>
        <input
          value={q}
          onChange={(e) => setQ(e.target.value)}
          placeholder="Search by status or tableID..."
          className="border rounded px-3 py-2 w-60"
        />
      </div>

      <div className="bg-white/90 rounded-lg shadow overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-slate-50">
            <tr>
              <th className="p-3">Order ID</th>
              <th className="p-3">Table ID</th>
              <th className="p-3">Status</th>
              <th className="p-3">Created At</th>
              <th className="p-3">Total Amount</th>
              <th className="p-3 w-48">Actions</th>
            </tr>
          </thead>
          <tbody>
            {current.map((o) => (
              <tr key={o.orderID} className="border-t hover:bg-slate-50">
                <td className="p-3 font-medium">{o.orderID}</td>
                <td className="p-3">{o.tableID}</td>
                <td className="p-3 text-slate-600">{o.status}</td>
                <td className="p-3">{new Date(o.createdAt).toLocaleString()}</td>
                <td className="p-3">{o.totalAmount.toFixed(2)}</td>
                <td className="p-3 space-x-2">
                  <button onClick={() => openModal(o)} className="px-3 py-1 rounded bg-blue-500 text-white">
                    Details
                  </button>
                  <button onClick={() => handleDelete(o.orderID)} className="px-3 py-1 rounded bg-red-600 text-white">
                    Delete
                  </button>
                </td>
              </tr>
            ))}
            {current.length === 0 && (
              <tr>
                <td colSpan={6} className="p-6 text-center text-slate-500">
                  Không có order nào
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      <div className="flex justify-center gap-2 mt-4">
        <button onClick={() => setPage((p) => Math.max(1, p - 1))} className="px-3 py-1 border rounded" disabled={page === 1}>Prev</button>
        {[...Array(totalPages)].map((_, i) => {
          const p = i + 1;
          return (
            <button key={p} onClick={() => setPage(p)} className={`px-3 py-1 border rounded ${p === page ? "bg-sky-600 text-white" : "bg-white"}`}>
              {p}
            </button>
          );
        })}
        <button onClick={() => setPage((p) => Math.min(totalPages, p + 1))} className="px-3 py-1 border rounded" disabled={page === totalPages}>Next</button>
      </div>

      {/* Modal xem chi tiết order */}
      {showModal && selectedOrder && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowModal(false)} />
          <div className="relative bg-white rounded-lg shadow-lg w-full max-w-lg p-6 z-10">
            <h3 className="text-xl font-bold mb-3">Order Details #{selectedOrder.orderID}</h3>
            <table className="w-full text-left mb-4">
              <thead className="bg-slate-50">
                <tr>
                  <th className="p-2">Dish</th>
                  <th className="p-2">Qty</th>
                  <th className="p-2">Price</th>
                  <th className="p-2">Amount</th>
                </tr>
              </thead>
              <tbody>
                {selectedOrder.orderItems.map((item) => (
                  <tr key={item.id} className="border-t">
                    <td className="p-2">{item.dish.name}</td>
                    <td className="p-2">{item.quantity}</td>
                    <td className="p-2">{item.price.toFixed(2)}</td>
                    <td className="p-2">{item.amount.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="text-right font-bold mb-2">Total: {selectedOrder.totalAmount.toFixed(2)}</div>
            <div className="flex justify-end gap-2">
              <button onClick={() => setShowModal(false)} className="px-4 py-2 rounded border">Close</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default OrdersPage;
