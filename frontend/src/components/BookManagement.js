import React, { useState, useEffect } from 'react';
import axios from 'axios';

function BookManagement() {
  const [books, setBooks] = useState([]);
  const [editingBook, setEditingBook] = useState(null);
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    isbn: ''
  });

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      const response = await axios.get('/api/books');
      setBooks(response.data);
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingBook) {
        await axios.put(`/api/books/${editingBook.id}`, formData);
        setEditingBook(null);
      } else {
        await axios.post('/api/books', formData);
      }
      setFormData({ title: '', author: '', isbn: '' });
      fetchBooks();
    } catch (error) {
      console.error('Error saving book:', error);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const editBook = (book) => {
    setEditingBook(book);
    setFormData({
      title: book.title,
      author: book.author,
      isbn: book.isbn
    });
  };

  const cancelEdit = () => {
    setEditingBook(null);
    setFormData({ title: '', author: '', isbn: '' });
  };

  const deleteBook = async (id) => {
    if (window.confirm('Are you sure you want to delete this book?')) {
      try {
        await axios.delete(`/api/books/${id}`);
        fetchBooks();
      } catch (error) {
        console.error('Error deleting book:', error);
      }
    }
  };

  return (
    <div className="section">
      <h2>📚 Book Management</h2>
      
      <form className="form" onSubmit={handleSubmit}>
        <input
          type="text"
          name="title"
          placeholder="Book Title"
          value={formData.title}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="author"
          placeholder="Author"
          value={formData.author}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="isbn"
          placeholder="ISBN"
          value={formData.isbn}
          onChange={handleChange}
          required
        />
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">
            {editingBook ? 'Update Book' : 'Add Book'}
          </button>
          {editingBook && (
            <button type="button" className="btn btn-secondary" onClick={cancelEdit}>
              Cancel
            </button>
          )}
        </div>
      </form>

      <div className="list">
        {books.length === 0 ? (
          <p className="no-books">No books found. Add your first book!</p>
        ) : (
          books.map(book => (
            <div key={book.id} className="item">
              <h3>{book.title}</h3>
              <p>✍️ Author: {book.author}</p>
              <p>📖 ISBN: {book.isbn}</p>
              <div className="actions">
                <button 
                  className="btn btn-edit" 
                  onClick={() => editBook(book)}
                >
                  Edit
                </button>
                <button 
                  className="btn btn-danger" 
                  onClick={() => deleteBook(book.id)}
                >
                  Delete
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default BookManagement;