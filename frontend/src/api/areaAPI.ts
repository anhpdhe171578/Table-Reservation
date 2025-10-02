// src/api/areaAPI.ts
import axiosClient from "./axiosClient";

export interface AreaDTO {
  id: number;
  areaName: string;
  restaurantID: number;
  createdAt: string;
  updatedAt: string;
}

// Lấy danh sách Area
export async function getAreas(): Promise<AreaDTO[]> {
  const res = await axiosClient.get<{ data: AreaDTO[] }>("/areas");
  return res.data.data; // vì BE bọc trong "data"
}

// Tạo mới Area
export async function createArea(payload: Omit<AreaDTO, "id" | "createdAt" | "updatedAt">) {
  const res = await axiosClient.post("/areas", payload);
  return res.data;
}

// Cập nhật Area
export async function updateArea(id: number, payload: Partial<AreaDTO>) {
  const res = await axiosClient.put(`/areas/${id}`, payload);
  return res.data;
}

// Xoá Area
export async function deleteArea(id: number) {
  await axiosClient.delete(`/areas/${id}`);
}
