import React, { useState } from "react";
import PropTypes from "prop-types";
import { createUser } from "../api";

const UserForm = ({ onUserAdded }) => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name.trim() || !email.trim() || !password.trim()) {
      alert("Please fill in all fields.");
      return;
    }
    setSubmitting(true);
    try {
      await createUser({ name: name.trim(), email: email.trim(), password });
      setName("");
      setEmail("");
      if (onUserAdded) onUserAdded();
    } catch (err) {
      console.error(err);
      alert("Failed to add user.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginTop: "20px" }}>
      <input
        type="text"
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
        style={{ marginRight: "10px" }}
        disabled={submitting}
      />
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
        style={{ marginRight: "10px" }}
        disabled={submitting}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        disabled={submitting}
        onChange={(e) => setPassword(e.target.value)}
        required
        style={{ marginRight: "10px" }}
      />
      <button type="submit" disabled={submitting}>
        {submitting ? "Adding..." : "Add User"}
      </button>
    </form>
  );
};

// Add Prop Validation
UserForm.propTypes = {
  onUserAdded: PropTypes.func,
}

export default UserForm;