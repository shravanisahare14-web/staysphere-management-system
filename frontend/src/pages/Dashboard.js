import React, { useState, useEffect } from 'react';
import { 
  Hotel, 
  Users, 
  CalendarCheck, 
  IndianRupee, 
  TrendingUp,
  BedDouble,
  ArrowUpRight,
  ArrowDownRight
} from 'lucide-react';

const API_URL = 'http://localhost:8080';

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [rooms, setRooms] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [statsRes, roomsRes, bookingsRes] = await Promise.all([
        fetch(`${API_URL}/api/stats`),
        fetch(`${API_URL}/api/rooms`),
        fetch(`${API_URL}/api/bookings`)
      ]);

      const statsData = await statsRes.json();
      const roomsData = await roomsRes.json();
      const bookingsData = await bookingsRes.json();

      setStats(statsData);
      setRooms(roomsData);
      setBookings(bookingsData.slice(0, 5));
      setLoading(false);
    } catch (err) {
      console.error('Error fetching data:', err);
      setLoading(false);
    }
  };

  const getRoomTypeCount = (type) => rooms.filter(r => r.roomType === type).length;
  const getAvailableByType = (type) => rooms.filter(r => r.roomType === type && r.available).length;

  const statCards = stats ? [
    { 
      icon: Hotel, 
      value: stats.totalRooms, 
      label: 'Total Rooms', 
      color: 'blue',
      sub: `${stats.availableRooms} available`
    },
    { 
      icon: Users, 
      value: stats.occupiedRooms, 
      label: 'Occupied', 
      color: 'yellow',
      sub: `${stats.occupancyRate.toFixed(1)}% occupancy`
    },
    { 
      icon: CalendarCheck, 
      value: stats.activeBookings, 
      label: 'Active Bookings', 
      color: 'green',
      sub: `${stats.totalBookings} total`
    },
    { 
      icon: IndianRupee, 
      value: `Rs ${stats.totalRevenue.toLocaleString('en-IN')}`, 
      label: 'Total Revenue', 
      color: 'purple',
      sub: 'Lifetime earnings'
    },
  ] : [];

  if (loading) return <div className="loading">Loading dashboard...</div>;

  return (
    <div className="dashboard-container">
      {/* Stats Grid */}
      <div className="stats-grid">
        {statCards.map((stat, index) => {
          const Icon = stat.icon;
          return (
            <div key={index} className={`stat-card ${stat.color}`}>
              <div className="stat-icon">
                <Icon size={24} />
              </div>
              <div className="stat-value">{stat.value}</div>
              <div className="stat-label">{stat.label}</div>
              <div style={{ fontSize: '12px', color: '#64748b', marginTop: '8px' }}>
                {stat.sub}
              </div>
            </div>
          );
        })}
      </div>

      {/* Room Availability by Type */}
      <div className="card">
        <div className="section-header">
          <h2 className="card-title">Room Availability by Type</h2>
        </div>
        {['Single', 'Double', 'Deluxe', 'Suite'].map(type => {
          const total = getRoomTypeCount(type);
          const avail = getAvailableByType(type);
          const percent = total > 0 ? ((total - avail) / total) * 100 : 0;
          return (
            <div key={type} style={{ marginBottom: '16px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px' }}>
                <span style={{ fontSize: '14px', fontWeight: '600', color: '#e2e8f0' }}>{type}</span>
                <span style={{ fontSize: '13px', color: '#94a3b8' }}>
                  {avail}/{total} available
                </span>
              </div>
              <div className="progress-bar">
                <div 
                  className="progress-fill" 
                  style={{ 
                    width: `${percent}%`, 
                    background: percent > 80 ? '#ef4444' : percent > 50 ? '#f59e0b' : '#10b981'
                  }}
                />
              </div>
            </div>
          );
        })}
      </div>

      {/* Recent Bookings */}
      <div className="card">
        <div className="section-header">
          <h2 className="card-title">Recent Bookings</h2>
        </div>
        {bookings.length === 0 ? (
          <div className="empty-state">
            <h3>No bookings yet</h3>
            <p>Create your first booking to see it here</p>
          </div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Booking ID</th>
                <th>Guest</th>
                <th>Room</th>
                <th>Dates</th>
                <th>Amount</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map(booking => (
                <tr key={booking.bookingId}>
                  <td style={{ fontWeight: '600', color: '#818cf8' }}>{booking.bookingId}</td>
                  <td>{booking.guestName}</td>
                  <td>{booking.roomId} ({booking.roomType})</td>
                  <td>{booking.checkInDate} to {booking.checkOutDate}</td>
                  <td style={{ fontWeight: '600', color: '#34d399' }}>
                    Rs {booking.totalAmount.toLocaleString('en-IN')}
                  </td>
                  <td>
                    <span className={`badge badge-${booking.bookingStatus.toLowerCase().replace('_', '-')}`}>
                      {booking.bookingStatus}
                    </span>
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

export default Dashboard;
