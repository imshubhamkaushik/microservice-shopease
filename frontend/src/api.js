import axios from "axios";

// Base URL for backend APIs
// - In Docker/K8s behind a reverse proxy, you can keep this as ""
// - For local development, set REACT_APP_API_BASE_URL in .env file
// const API_BASE = process.env.REACT_APP_API_BASE_URL || "";
const USER_API_BASE =
  process.env.REACT_APP_USER_API_BASE_URL || "http://localhost:8081";
const PRODUCT_API_BASE =
  process.env.REACT_APP_PRODUCT_API_BASE_URL || "http://localhost:8082";

// --------USER APIs--------

export const getUsers = async () => {
  const res = await axios.get(`${USER_API_BASE}/users`);
  return res.data;
};

export const createUser = async (user) => {
  const res = await axios.post(`${USER_API_BASE}/users/register`, user);
  return res.data;
};

export const deleteUser = async (id) => {
  const res = await axios.delete(`${USER_API_BASE}/users/${id}`);
  return res.data;
};

export const loginUser = async (credentials) => {
  const res = await axios.delete(`${USER_API_BASE}/users/login`, credentials);
  return res.data;
}

// --------PRODUCT APIs--------

export const getProducts = async () => {
  const res = await axios.get(`${PRODUCT_API_BASE}/products`);
  return res.data;
};

export const createProduct = async (product) => {
  const res = await axios.post(`${PRODUCT_API_BASE}/products`, product);
  return res.data;
};

export const deleteProduct = async (id) => {
  const res = await axios.delete(`${PRODUCT_API_BASE}/products/${id}`);
  return res.data;
};
