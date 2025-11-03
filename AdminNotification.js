import React, { useEffect, useState } from 'react';
import { Bell, X, UserPlus, FileText } from 'lucide-react';
import './AdminNotification.css';
import notificationService from '../../../services/notificationService';

const AdminNotification = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    let mounted = true;
    const mapTypeIcon = (type) => {
      if (type === 'SUBSCRIPTION_REQUEST') return UserPlus;
      if (type === 'FILE_ADDED') return FileText;
      return Bell;
    };
    const load = async () => {
      try {
        const data = await notificationService.getAdmin(false);
        if (!mounted) return;
        const mapped = data.map(n => ({
          id: n.id,
          type: (n.type || '').toLowerCase().includes('subscription') ? 'subscription' : (n.type === 'FILE_ADDED' ? 'file' : 'info'),
          icon: mapTypeIcon(n.type),
          title: n.title,
          message: n.message,
          time: new Date(n.createdAt).toLocaleString(),
          isNew: !n.readFlag
        }));
        setNotifications(mapped);
      } catch (e) {
        // silent
      }
    };
    load();
    const t = setInterval(load, 15000);
    return () => { mounted = false; clearInterval(t); };
  }, []);

  const unreadCount = notifications.filter(n => n.isNew).length;

  const markAsRead = async (id) => {
    setNotifications(prev => prev.map(n => n.id === id ? { ...n, isNew: false } : n));
    try { await notificationService.markRead(id); } catch { }
  };

  const markAllAsRead = async () => {
    setNotifications(prev => prev.map(n => ({ ...n, isNew: false })));
    try { await notificationService.markAllAdmin(); } catch { }
  };

  const removeNotification = (id) => {
    setNotifications(notifications.filter(n => n.id !== id));
  };

  return (
    <div className="admin-notif-container">
      <div className="admin-notif-trigger" onClick={() => setIsOpen(!isOpen)}>
        <Bell size={20} />
        {unreadCount > 0 && (
          <span className="admin-notif-badge">{unreadCount}</span>
        )}
      </div>

      {isOpen && (
        <div className="admin-notif-dropdown">
          <div className="admin-notif-header">
            <h5 className="admin-notif-title">Notifications</h5>
            {unreadCount > 0 && (
              <button className="admin-mark-all" onClick={markAllAsRead}>
                Mark all read
              </button>
            )}
          </div>

          <div className="admin-notif-list">
            {notifications.length === 0 ? (
              <div className="admin-no-notif">
                <Bell size={40} />
                <p>No notifications</p>
              </div>
            ) : (
              notifications.map(notification => {
                const Icon = notification.icon;
                return (
                  <div
                    key={notification.id}
                    className={`admin-notif-item ${notification.isNew ? 'unread' : ''}`}
                    onClick={() => markAsRead(notification.id)}
                  >
                    <div className={`admin-notif-icon ${notification.type}`}>
                      <Icon size={18} />
                    </div>
                    <div className="admin-notif-content">
                      <h6>{notification.title}</h6>
                      <p>{notification.message}</p>
                      <span className="admin-notif-time">{notification.time}</span>
                    </div>
                    <button
                      className="admin-notif-close"
                      onClick={(e) => {
                        e.stopPropagation();
                        removeNotification(notification.id);
                      }}
                    >
                      <X size={16} />
                    </button>
                  </div>
                );
              })
            )}
          </div>

          {notifications.length > 0 && (
            <div className="admin-notif-footer">
              <a href="#" className="admin-view-all">View All Activity</a>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default AdminNotification;