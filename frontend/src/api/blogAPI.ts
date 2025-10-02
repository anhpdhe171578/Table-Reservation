// src/api/blogAPI.ts
import axiosClient from "./axiosClient";

export interface BlogDTO {
  id: number;
  image: string;
  title: string;
  subTitle: string;
  description: string;
  createAtBy: string;
  status: string;
  createdAt: string;
  updatedAt: string;
}

// Lấy danh sách blog
export async function getBlogs(): Promise<BlogDTO[]> {
  const res = await axiosClient.get("/blogs");
  return res.data.data;
}

// Tạo mới blog
export async function createBlog(payload: Omit<BlogDTO, "id" | "createdAt" | "updatedAt">) {
  const res = await axiosClient.post("/blogs", payload);
  return res.data.data;
}

// Cập nhật blog
export async function updateBlog(id: number, payload: Partial<BlogDTO>) {
  const res = await axiosClient.put(`/blogs/${id}`, payload);
  return res.data.data;
}

// Xoá blog
export async function deleteBlog(id: number) {
  const res = await axiosClient.delete(`/blogs/${id}`);
  return res.data.data;
}
