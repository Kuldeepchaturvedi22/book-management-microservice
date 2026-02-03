import React, { useState } from 'react';
import axios from 'axios';

function Login({ onLogin }) {
  const [isRegister, setIsRegister] = useState(false);
  const [formData, setFormData] = useState({
    username: '',  // <--- CHANGED from 'name' to 'username'
    email: '',
    password: '',
    role: 'BUYER'  // ✅ Uppercase is correct!
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const endpoint = isRegister ? '/api/users/register' : '/api/users/login';

      // We send 'formData' directly.
      // Since we renamed the field to 'username', it now matches the Backend!
      const payload = isRegister ? formData : { email: formData.email, password: formData.password };

      const response = await axios.post(endpoint, payload);
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
      onLogin(response.data.user);
    } catch (error) {
      console.error("Login Error:", error); // Added logging to see the real error in Console
      alert(error.response?.data?.error || 'Authentication failed');
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
      <div className="login-container">
        <div className="login-box">
          <h2>{isRegister ? '📝 Register' : '🔐 Login'}</h2>
          <form onSubmit={handleSubmit}>
            {isRegister && (
                <input
                    type="text"
                    name="username"  // <--- CHANGED from 'name' to 'username'
                    placeholder="Username"
                    value={formData.username} // <--- Update state reference
                    onChange={handleChange}
                    required
                />
            )}
            <input
                type="email"
                name="email"
                placeholder="Email"
                value={formData.email}
                onChange={handleChange}
                required
            />
            <input
                type="password"
                name="password"
                placeholder="Password"
                value={formData.password}
                onChange={handleChange}
                required
            />
            {isRegister && (
                <select name="role" value={formData.role} onChange={handleChange}>
                  <option value="BUYER">Buyer</option>
                  <option value="SELLER">Seller</option>
                </select>
            )}
            <button type="submit" className="btn btn-primary">
              {isRegister ? 'Register' : 'Login'}
            </button>
          </form>
          <p className="toggle-auth">
            {isRegister ? 'Already have an account?' : "Don't have an account?"}
            <button onClick={() => setIsRegister(!isRegister)} className="link-btn">
              {isRegister ? 'Login' : 'Register'}
            </button>
          </p>
        </div>
      </div>
  );
}

export default Login;