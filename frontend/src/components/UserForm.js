import React, { useState } from "react";
import { createUser } from "../api";

const UserForm = ({ onUserAdded }) => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    await createUser({ name, email });
    setName("");
    setEmail("");
    onUserAdded(); // Refresh list
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
      <input
        type="text"
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
        style={{ marginRight: "10px" }}
      />
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
        style={{ marginRight: "10px" }}
      />
      <button type="submit">Add User</button>
    </form>
  );
};

export default UserForm;
