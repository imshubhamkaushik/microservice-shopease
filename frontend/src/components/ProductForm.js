import React, { useState } from "react";
import { createProduct } from "../api";

const ProductForm = ({ onProductAdded }) => {
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    await createProduct({ name, price });
    setName("");
    setPrice("");
    onProductAdded(); // Refresh list
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
      <input
        type="text"
        placeholder="Product Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
        style={{ marginRight: "10px" }}
      />
      <input
        type="number"
        placeholder="Price"
        value={price}
        onChange={(e) => setPrice(e.target.value)}
        required
        style={{ marginRight: "10px" }}
      />
      <button type="submit">Add Product</button>
    </form>
  );
};

export default ProductForm;
