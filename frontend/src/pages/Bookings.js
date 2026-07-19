import React, { useState, useEffect } from 'react';
import { CheckCircle, LogOut, XCircle, RefreshCw } from 'lucide-react';

const API_URL = 'http://localhost:8080';

const Bookings = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      const res = await fetch(`${API_URL}/api/bookings`);
      const data = await res.json();
      setBookings(data);
      setLoading(false);
    } catch (err) {
      console.error(err);
      setLoading(false);
    }
  };

  const handleAction = async (bookingId, action) => {
    try {
      const res = await fetch(`${API_URL}/api/bookings/${action}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ bookingId })
      });
      const data = await res.json();
      setMessage(data.message);
      fetchBookings();
      setTimeout(() => setMessage(''), 3000);
    } catch (err) {
      setMessage('Error: ' + err.message);
    }
  };

  if (loading) return <div className="loading">Loading bookings...</div>;

  return (
    <div className="dashboard-container">
      <div className="section-header">
        <h2 className="section-title">All Bookings</h2>
        <button onClick={fetchBookings} className="btn btn-sm" style={{ background: 'rgba(255,255,255,0.05)' }}>
          <RefreshCw size={14} /> Refresh
        </button>
      </div>

      {message && (
        <div className={`toast ${message.includes('Error') ? 'toast-error' : 'toast-success'}`}>
          {message}
        </div>
      )}

      <div className="card">
        {bookings.length === 0 ? (
          <div className="empty-state">
            <h3>No bookings found</h3>
            <p>Create a new booking to get started</p>
          </div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Booking ID</th>
                <th>Guest</th>
                <th>Room</th>
                <th>Check In</th>
                <th>Check Out</th>
                <th>Guests</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map(b => (
                <tr key={b.bookingId}>
                  <td style={{ fontWeight: '600', color: '#818cf8' }}>{b.bookingId}</td>
                  <td>{b.guestName}</td>
                  <td>{b.roomId}</td>
                  <td>{b.checkInDate}</td>
                  <td>{b.checkOutDate}</td>
                  <td>{b.numberOfGuests}</td>
                  <td style={{ fontWeight: '600', color: '#34d399' }}>
                    Rs {b.totalAmount.toLocaleString('en-IN')}
                  </td>
                  <td>
                    <span className={`badge badge-${b.bookingStatus.toLowerCase().replace('_', '-')}`}>
                      {b.bookingStatus}
                    </span>
                  </td>
                  <td>
                    <div style={{ display: 'flex', gap: '6px' }}>
                      {b.bookingStatus === 'CONFIRMED' && (
                        <>
                          <button onClick={() => handleAction(b.bookingId, 'checkin')} className="btn btn-success btn-sm">
                            <CheckCircle size={12} /> Check In
                          </button>
                          <button onClick={() => handleAction(b.bookingId, 'cancel')} className="btn btn-danger btn-sm">
                            <XCircle size={12} /> Cancel
                          </button>
                        </>
                      )}
                      {b.bookingStatus === 'CHECKED_IN' && (
                        <button onClick={() => handleAction(b.bookingId, 'checkout')} className="btn btn-primary btn-sm">
                          <LogOut size={12} /> Check Out
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default Bookings;
