import React, { useState, useEffect } from 'react';
import { Users } from 'lucide-react';

const API_URL = 'http://localhost:8080';

const Rooms = () => {
  const [rooms, setRooms] = useState([]);
  const [filter, setFilter] = useState('All');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRooms();
  }, []);

  const fetchRooms = async () => {
    try {
      const res = await fetch(`${API_URL}/api/rooms`);
      const data = await res.json();
      setRooms(data);
      setLoading(false);
    } catch (err) {
      console.error(err);
      setLoading(false);
    }
  };

  const filteredRooms = filter === 'All' ? rooms : rooms.filter(r => r.roomType === filter);
  const types = ['All', ...new Set(rooms.map(r => r.roomType))];

  if (loading) return <div className="loading">Loading rooms...</div>;

  return (
    <div className="dashboard-container">
      <div className="section-header">
        <h2 className="section-title">All Rooms</h2>
        <div style={{ display: 'flex', gap: '8px' }}>
          {types.map(type => (
            <button
              key={type}
              onClick={() => setFilter(type)}
              className="btn btn-sm"
              style={{
                background: filter === type ? 'linear-gradient(135deg, #6366f1, #8b5cf6)' : 'rgba(255,255,255,0.05)',
                color: filter === type ? '#fff' : '#94a3b8'
              }}
            >
              {type}
            </button>
          ))}
        </div>
      </div>

      <div className="rooms-grid">
        {filteredRooms.map(room => (
          <div key={room.roomId} className="room-card">
            <img src={room.imageUrl} alt={room.roomType} className="room-image" />
            <div className="room-info">
              <div className="room-type">{room.roomType}</div>
              <div className="room-id">Room {room.roomId}</div>
              <div className="room-price">
                Rs {room.pricePerNight.toLocaleString('en-IN')}
                <span>/night</span>
              </div>
              <div className="room-desc">{room.description}</div>
              <div className="room-amenities">{room.amenities}</div>
              <div className="room-footer">
                <span className="room-capacity">
                  <Users size={14} /> Max {room.maxCapacity} guests
                </span>
                <span className={`badge ${room.available ? 'badge-confirmed' : 'badge-cancelled'}`}>
                  {room.available ? 'Available' : 'Booked'}
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Rooms;
