import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Calendar, Users, User, Phone, Mail, CreditCard, MapPin, MessageSquare } from 'lucide-react';

const API_URL = 'http://localhost:8080';

const NewBooking = () => {
  const [rooms, setRooms] = useState([]);
  const [formData, setFormData] = useState({
    guestName: '', phone: '', email: '', idProof: 'Aadhar', idProofNumber: '', address: '',
    roomId: '', checkInDate: '', checkOutDate: '', numberOfGuests: 1, specialRequests: ''
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetch(`${API_URL}/api/rooms/available`)
      .then(r => r.json())
      .then(data => setRooms(data));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await fetch(`${API_URL}/api/bookings/create`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });
      const data = await res.json();
      setMessage(data.message);
      if (data.success) {
        setTimeout(() => navigate('/bookings'), 2000);
      }
    } catch (err) {
      setMessage('Error creating booking');
    }
    setLoading(false);
  };

  return (
    <div className="dashboard-container">
      <h2 className="section-title" style={{ marginBottom: '24px' }}>Create New Booking</h2>

      {message && (
        <div className={`toast ${message.includes('Error') || message.includes('success') === false ? 'toast-error' : 'toast-success'}`}>
          {message}
        </div>
      )}

      <form onSubmit={handleSubmit} className="card">
        <div className="form-grid">
          <div className="form-group">
            <label className="form-label"><User size={16} /> Full Name</label>
            <input className="form-input" placeholder="Enter guest name" required
              value={formData.guestName} onChange={e => setFormData({...formData, guestName: e.target.value})} />
          </div>
          <div className="form-group">
            <label className="form-label"><Phone size={16} /> Phone Number</label>
            <input className="form-input" placeholder="10 digit number" required
              value={formData.phone} onChange={e => setFormData({...formData, phone: e.target.value})} />
          </div>
          <div className="form-group">
            <label className="form-label"><Mail size={16} /> Email</label>
            <input className="form-input" type="email" placeholder="guest@email.com" required
              value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} />
          </div>
          <div className="form-group">
            <label className="form-label"><CreditCard size={16} /> ID Proof</label>
            <select className="form-select"
              value={formData.idProof} onChange={e => setFormData({...formData, idProof: e.target.value})}>
              <option>Aadhar</option>
              <option>Passport</option>
              <option>Driving License</option>
              <option>Voter ID</option>
              <option>PAN</option>
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">ID Number</label>
            <input className="form-input" placeholder="ID proof number" required
              value={formData.idProofNumber} onChange={e => setFormData({...formData, idProofNumber: e.target.value})} />
          </div>
          <div className="form-group">
            <label className="form-label"><MapPin size={16} /> Address</label>
            <input className="form-input" placeholder="Full address" required
              value={formData.address} onChange={e => setFormData({...formData, address: e.target.value})} />
          </div>
          <div className="form-group">
            <label className="form-label">Select Room</label>
            <select className="form-select" required
              value={formData.roomId} onChange={e => setFormData({...formData, roomId: e.target.value})}>
              <option value="">Choose a room</option>
              {rooms.map(r => (
                <option key={r.roomId} value={r.roomId}>
                  {r.roomId} - {r.roomType} (Rs {r.pricePerNight}/night, Max {r.maxCapacity} guests)
                </option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label className="form-label"><Users size={16} /> Number of Guests</label>
            <input className="form-input" type="number" min="1" max="10" required
              value={formData.numberOfGuests} onChange={e => setFormData({...formData, numberOfGuests: parseInt(e.target.value)})} />
          </div>
          <div className="form-group">
            <label className="form-label"><Calendar size={16} /> Check-In Date</label>
            <input className="form-input" type="date" required
              value={formData.checkInDate} onChange={e => setFormData({...formData, checkInDate: e.target.value})} />
          </div>
          <div className="form-group">
            <label className="form-label"><Calendar size={16} /> Check-Out Date</label>
            <input className="form-input" type="date" required
              value={formData.checkOutDate} onChange={e => setFormData({...formData, checkOutDate: e.target.value})} />
          </div>
        </div>
        <div className="form-group" style={{ marginTop: '20px' }}>
          <label className="form-label"><MessageSquare size={16} /> Special Requests</label>
          <input className="form-input" placeholder="Any special requests..."
            value={formData.specialRequests} onChange={e => setFormData({...formData, specialRequests: e.target.value})} />
        </div>
        <button type="submit" className="btn btn-primary" style={{ marginTop: '24px', width: '100%', justifyContent: 'center' }} disabled={loading}>
          {loading ? 'Creating...' : 'Create Booking'}
        </button>
      </form>
    </div>
  );
};

export default NewBooking;
