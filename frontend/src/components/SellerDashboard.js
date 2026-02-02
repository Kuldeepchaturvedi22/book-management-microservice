import React, { useState, useEffect } from 'react';
import axios from 'axios';

function SellerDashboard({ user }) {
  const [books, setBooks] = useState([]);
  const [orders, setOrders] = useState([]);
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    isbn: '',
    price: '',
    quantity: ''
  });
  const [editingBook, setEditingBook] = useState(null);

  useEffect(() => {
    fetchBooks();
    fetchOrders();
  }, []);

  const fetchBooks = async () => {
    try {
      const response = await axios.get(`/api/books/seller/${user.id}`);
      setBooks(response.data);
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  const fetchOrders = async () => {
    try {
      const response = await axios.get(`/api/orders/seller/${user.id}`);
      setOrders(response.data);
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const bookData = { ...formData, sellerId: user.id };
      if (editingBook) {
        await axios.put(`/api/books/${editingBook.id}`, bookData);
        setEditingBook(null);
      } else {
        await axios.post('/api/books', bookData);
      }
      setFormData({ title: '', author: '', isbn: '', price: '', quantity: '' });
      fetchBooks();
    } catch (error) {
      console.error('Error saving book:', error);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const editBook = (book) => {
    setEditingBook(book);
    setFormData({
      title: book.title,
      author: book.author,
      isbn: book.isbn,
      price: book.price,
      quantity: book.quantity
    });
  };

  const deleteBook = async (id) => {
    if (window.confirm('Delete this book?')) {
      try {
        await axios.delete(`/api/books/${id}`);
        fetchBooks();
      } catch (error) {
        console.error('Error deleting book:', error);
      }
    }
  };

  return (
    <div>
      <h2>📚 Seller Dashboard</h2>
      
      <div className="section">
        <h3>List New Book</h3>
        <form className="form" onSubmit={handleSubmit}>
          <input type="text" name="title" placeholder="Title" value={formData.title} onChange={handleChange} required />
          <input type="text" name="author" placeholder="Author" value={formData.author} onChange={handleChange} required />
          <input type="text" name="isbn" placeholder="ISBN" value={formData.isbn} onChange={handleChange} required />
          <input type="number" name="price" placeholder="Price" value={formData.price} onChange={handleChange} required step="0.01" />
          <input type="number" name="quantity" placeholder="Quantity" value={formData.quantity} onChange={handleChange} required />
          <button type="submit" className="btn btn-primary">
            {editingBook ? 'Update' : 'Add Book'}
          </button>
          {editingBook && <button type="button" className="btn btn-secondary" onClick={() => { setEditingBook(null); setFormData({ title: '', author: '', isbn: '', price: '', quantity: '' }); }}>Cancel</button>}
        </form>
      </div>

      <div className="section">
        <h3>My Books ({books.length})</h3>
        <div className="list">
          {books.map(book => (
            <div key={book.id} className="item">
              <h4>{book.title}</h4>
              <p>✍️ {book.author} | 📖 {book.isbn}</p>
              <p>💰 ${book.price} | 📦 Stock: {book.quantity} | {book.status}</p>
              <div className="actions">
                <button className="btn btn-edit" onClick={() => editBook(book)}>Edit</button>
                <button className="btn btn-danger" onClick={() => deleteBook(book.id)}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="section">
        <h3>Sales ({orders.length})</h3>
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

export default SellerDashboard;
