const API_BASE_URL = 'http://localhost:8080/api/notifications';

const notificationService = {
    getAdmin: async (unreadOnly = false) => {
        const res = await fetch(`${API_BASE_URL}/admin?unreadOnly=${unreadOnly}`);
        if (!res.ok) throw new Error('Failed to load admin notifications');
        return res.json();
    },

    getUser: async (email, unreadOnly = false) => {
        const res = await fetch(`${API_BASE_URL}/user/${encodeURIComponent(email)}?unreadOnly=${unreadOnly}`);
        if (!res.ok) throw new Error('Failed to load user notifications');
        return res.json();
    },

    markRead: async (id) => {
        const res = await fetch(`${API_BASE_URL}/${encodeURIComponent(id)}/read`, { method: 'PUT' });
        if (!res.ok) throw new Error('Failed to mark as read');
    },

    markAllAdmin: async () => {
        const res = await fetch(`${API_BASE_URL}/read-all/admin`, { method: 'PUT' });
        if (!res.ok) throw new Error('Failed to mark all read');
        return res.json();
    },

    markAllUser: async (email) => {
        const res = await fetch(`${API_BASE_URL}/read-all/user/${encodeURIComponent(email)}`, { method: 'PUT' });
        if (!res.ok) throw new Error('Failed to mark all read');
        return res.json();
    }
};

export default notificationService;


