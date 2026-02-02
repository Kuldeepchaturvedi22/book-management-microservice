import React, { useState, useEffect } from 'react';
import axios from 'axios';

function BuyerDashboard({ user }) {
  const [books, setBooks] = useState([]);
  const [orders, setOrders] = useState([]);
  const [cart, setCart] = useState({});

  useEffect(() => {
    fetchBooks();
    fetchOrders();
  }, []);

  const fetchBooks = async () => {
    try {
      const response = await axios.get('/api/books/available');
      setBooks(response.data);
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  const fetchOrders = async () => {
    try {
      const response = await axios.get(`/api/orders/buyer/${user.id}`);
      setOrders(response.data);
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  const addToCart = (bookId) => {
    setCart({ ...cart, [bookId]: (cart[bookId] || 0) + 1 });
  };

  const removeFromCart = (bookId) => {
    const newCart = { ...cart };
    if (newCart[bookId] > 1) {
      newCart[bookId]--;
    } else {
      delete newCart[bookId];
    }
    setCart(newCart);
  };

  const purchaseBook = async (bookId, quantity) => {
    try {
      await axios.post('/api/orders/purchase', {
        buyerId: user.id,
        bookId: bookId,
        quantity: quantity
      });
      alert('Purchase successful!');
      const newCart = { ...cart };
      delete newCart[bookId];
      setCart(newCart);
      fetchBooks();
      fetchOrders();
    } catch (error) {
      alert(error.response?.data?.error || 'Purchase failed');
    }
  };

  return (
    <div>
      <h2>🛒 Buyer Dashboard</h2>
      
      <div className="section">
        <h3>Available Books</h3>
        <div className="list">
          {books.map(book => (
            <div key={book.id} className="item">
              <h4>{book.title}</h4>
              <p>✍️ {book.author} | 📖 {book.isbn}</p>
              <p>💰 ${book.price} | 📦 Available: {book.quantity}</p>
              <div className="actions">
                {cart[book.id] ? (
                  <>
                    <button className="btn btn-secondary" onClick={() => removeFromCart(book.id)}>-</button>
                    <span style={{margin: '0 10px'}}>Qty: {cart[book.id]}</span>
                    <button className="btn btn-secondary" onClick={() => addToCart(book.id)}>+</button>
                    <button className="btn btn-primary" onClick={() => purchaseBook(book.id, cart[book.id])}>
                      Buy ${(book.price * cart[book.id]).toFixed(2)}
                    </button>
                  </>
                ) : (
                  <button className="btn btn-primary" onClick={() => addToCart(book.id)}>Add to Cart</button>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="section">
        <h3>My Orders ({orders.length})</h3>
        <div className="list">
          {orders.map(order => (
            <div key={order.id} className="item">
              <p>Order #{order.id} | Book ID: {order.bookId}</p>
              <p>Qty: {order.quantity} | Total: ${order.totalPrice}</p>
              <p>Status: {order.status} | {new Date(order.orderDate).toLocaleDateString()}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default BuyerDashboard;
