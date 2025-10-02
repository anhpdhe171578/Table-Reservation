// src/api/tableAPI.ts
import axiosClient from "./axiosClient";

export interface TableDTO {
  id: number;
  tableNumber: string;
  status: string;
  areaID: number;
  createdAt: string;
  updatedAt: string;
  numberOfDesk: number;
}

// Lấy danh sách bàn
export async function getTables(): Promise<TableDTO[]> {
  const res = await axiosClient.get("/tables");
  return res.data.data; // 👈 lấy mảng từ field "data"
}

// Tạo mới bàn
export async function createTable(payload: Omit<TableDTO, "id" | "createdAt" | "updatedAt">) {
  const res = await axiosClient.post("/tables", payload);
  return res.data.data; // 👈 thường backend cũng bọc trong data
}

// Cập nhật bàn
export async function updateTable(id: number, payload: Partial<TableDTO>) {
  const res = await axiosClient.put(`/tables/${id}`, payload);
  return res.data.data;
}

// Xoá bàn
export async function deleteTable(id: number) {
  const res = await axiosClient.delete(`/tables/${id}`);
  return res.data.data;
}
