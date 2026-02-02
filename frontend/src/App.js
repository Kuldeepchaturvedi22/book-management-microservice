import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import SellerDashboard from './components/SellerDashboard';
import BuyerDashboard from './components/BuyerDashboard';
import './App.css';

function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
  }, []);

  const handleLogin = (userData) => {
    setUser(userData);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  };

  if (!user) {
    return <Login onLogin={handleLogin} />;
  }

  return (
    <div className="App">
      <header className="header">
        <h1>📚 Book Management System</h1>
        <div className="user-info">
          <span>👤 {user.name} ({user.role})</span>
          <button onClick={handleLogout} className="btn btn-secondary">Logout</button>
        </div>
      </header>
      <main className="main">
        {user.role === 'SELLER' ? (
          <SellerDashboard user={user} />
        ) : (
          <BuyerDashboard user={user} />
        )}
      </main>
    </div>
  );
}

export default App;