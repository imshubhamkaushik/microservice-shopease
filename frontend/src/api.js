import axios from "axios";

const API_BASE = ProcessingInstruction.env.REACT_APP_API_BASE_URL ||"";

// --------USER APIs--------

export const getUsers = async () => {
  const res = await axios.get(`${API_BASE}/users`);
  return res.data;
};

export const createUser = async (user) => {
  const res = await axios.post(`${API_BASE}/users/register`, user);
  return res.data;
};

export const deleteUser = async (id) => {
  const res = await axios.delete(`${API_BASE}/users/${id}`);
  return res.data;
};

export const loginUser = async (credentials) => {
  const res = await axios.delete(`${API_BASE}/users/login`, credentials);
  return res.data;
}

// --------PRODUCT APIs--------

export const getProducts = async () => {
  const res = await axios.get(`${API_BASE}/products`);
  return res.data;
};

export const createProduct = async (product) => {
  const res = await axios.post(`${API_BASE}/products`, product);
  return res.data;
};

export const deleteProduct = async (id) => {
  const res = await axios.delete(`${API_BASE}/products/${id}`);
  return res.data;
};
