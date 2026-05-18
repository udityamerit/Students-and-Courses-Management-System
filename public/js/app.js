const app = {
    init() {
        this.bindNavigation();
        this.bindForms();
        
        // Initial load
        this.loadStudents();
        this.loadInstructors();
        this.loadCourses();
        this.loadEnrollments();
    },

    bindNavigation() {
        const links = document.querySelectorAll('.nav-links a');
        links.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const targetId = link.getAttribute('data-target');
                this.navigate(targetId);
            });
        });

        // Add buttons
        document.getElementById('btn-add-student').addEventListener('click', () => this.showForm('form-student'));
        document.getElementById('btn-add-instructor').addEventListener('click', () => this.showForm('form-instructor'));
        document.getElementById('btn-add-course').addEventListener('click', () => this.showForm('form-course'));
        document.getElementById('btn-add-enrollment').addEventListener('click', () => this.showForm('form-enrollment'));
    },

    navigate(targetId) {
        const link = document.querySelector(`[data-target='${targetId}']`);
        if (link) {
            // Update active state on links
            document.querySelectorAll('.nav-links a').forEach(l => l.classList.remove('active'));
            link.classList.add('active');
        }

        // Show target section, hide others
        document.querySelectorAll('.content-section').forEach(sec => {
            sec.classList.remove('active');
        });
        const targetSection = document.getElementById(targetId);
        if (targetSection) {
            targetSection.classList.add('active');
        }
        
        // Hide any open forms when switching tabs
        document.querySelectorAll('.form-container').forEach(form => {
            form.style.display = 'none';
        });
    },

    bindForms() {
        const forms = ['add-student-form', 'add-instructor-form', 'add-course-form', 'add-enrollment-form'];
        const endpoints = {
            'add-student-form': '/api/students',
            'add-instructor-form': '/api/instructors',
            'add-course-form': '/api/courses',
            'add-enrollment-form': '/api/enrollments'
        };

        forms.forEach(formId => {
            const form = document.getElementById(formId);
            if(form) {
                form.addEventListener('submit', async (e) => {
                    e.preventDefault();
                    const formData = new FormData(form);
                    const data = Object.fromEntries(formData);
                    
                    try {
                        // The Java backend expects simple flat JSON
                        const response = await fetch(endpoints[formId], {
                            method: 'POST',
                            body: JSON.stringify(data)
                        });

                        const result = await response.json();
                        
                        if (response.ok) {
                            this.showToast(result.message || 'Success!', 'success');
                            form.reset();
                            this.hideForm(form.parentElement.id);
                            
                            // Reload data
                            if(formId === 'add-student-form') this.loadStudents();
                            if(formId === 'add-instructor-form') this.loadInstructors();
                            if(formId === 'add-course-form') this.loadCourses();
                            if(formId === 'add-enrollment-form') this.loadEnrollments();
                        } else {
                            this.showToast(result.error || 'Operation failed', 'error');
                        }
                    } catch (err) {
                        this.showToast('Network error: ' + err.message, 'error');
                    }
                });
            }
        });
    },

    showForm(formId) {
        document.getElementById(formId).style.display = 'block';
    },

    hideForm(formId) {
        document.getElementById(formId).style.display = 'none';
    },

    showToast(message, type = 'success') {
        const toast = document.getElementById('toast');
        const icon = type === 'success' 
            ? `<svg viewBox="0 0 24 24" width="20" height="20" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>` 
            : `<svg viewBox="0 0 24 24" width="20" height="20" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>`;
            
        toast.innerHTML = `${icon} <span>${message}</span>`;
        toast.className = `toast show ${type}`;
        
        setTimeout(() => {
            toast.classList.remove('show');
        }, 3000);
    },

    // Data Loading Methods
    async loadStudents() {
        try {
            const res = await fetch('/api/students');
            const data = await res.json();
            const tbody = document.querySelector('#students-table tbody');
            tbody.innerHTML = '';
            
            // Update Dashboard Stat
            const statEl = document.getElementById('stat-students');
            if (statEl) statEl.innerText = data.length;
            
            if (data.length === 0) {
                tbody.innerHTML = `<tr><td colspan="5" class="empty-state">No students found. Add one to get started!</td></tr>`;
                return;
            }
            
            data.forEach((student, index) => {
                const tr = document.createElement('tr');
                tr.style.animationDelay = `${index * 0.05}s`;
                tr.innerHTML = `
                    <td>${student.id}</td>
                    <td>${student.regNo}</td>
                    <td>${student.firstName} ${student.lastName}</td>
                    <td>${student.email}</td>
                    <td><span class="badge badge-${student.status.toLowerCase()}">${student.status}</span></td>
                `;
                tbody.appendChild(tr);
            });
        } catch (e) {
            console.error('Failed to load students:', e);
        }
    },

    async loadInstructors() {
        try {
            const res = await fetch('/api/instructors');
            const data = await res.json();
            const tbody = document.querySelector('#instructors-table tbody');
            tbody.innerHTML = '';
            
            if (data.length === 0) {
                tbody.innerHTML = `<tr><td colspan="4" class="empty-state">No instructors found. Add one to get started!</td></tr>`;
                return;
            }
            
            data.forEach((inst, index) => {
                const tr = document.createElement('tr');
                tr.style.animationDelay = `${index * 0.05}s`;
                tr.innerHTML = `
                    <td>${inst.fid}</td>
                    <td>${inst.firstName} ${inst.lastName}</td>
                    <td>${inst.email}</td>
                    <td>${inst.department}</td>
                `;
                tbody.appendChild(tr);
            });
        } catch (e) {
            console.error('Failed to load instructors:', e);
        }
    },

    async loadCourses() {
        try {
            const res = await fetch('/api/courses');
            const data = await res.json();
            const tbody = document.querySelector('#courses-table tbody');
            tbody.innerHTML = '';
            
            // Update Dashboard Stat
            const statEl = document.getElementById('stat-courses');
            if (statEl) statEl.innerText = data.length;
            
            if (data.length === 0) {
                tbody.innerHTML = `<tr><td colspan="6" class="empty-state">No courses found. Add one to get started!</td></tr>`;
                return;
            }
            
            data.forEach((course, index) => {
                const tr = document.createElement('tr');
                tr.style.animationDelay = `${index * 0.05}s`;
                tr.innerHTML = `
                    <td>${course.code}</td>
                    <td>${course.title}</td>
                    <td>${course.credits}</td>
                    <td>${course.department}</td>
                    <td>${course.semester}</td>
                    <td>${course.instructor}</td>
                `;
                tbody.appendChild(tr);
            });
        } catch (e) {
            console.error('Failed to load courses:', e);
        }
    },

    async loadEnrollments() {
        try {
            const res = await fetch('/api/enrollments');
            const data = await res.json();
            const tbody = document.querySelector('#enrollments-table tbody');
            tbody.innerHTML = '';
            
            // Update Dashboard Stat
            const statEl = document.getElementById('stat-enrollments');
            if (statEl) statEl.innerText = data.length;
            
            if (data.length === 0) {
                tbody.innerHTML = `<tr><td colspan="5" class="empty-state">No enrollments found. Enroll a student to get started!</td></tr>`;
                return;
            }
            
            data.forEach((enrollment, index) => {
                const tr = document.createElement('tr');
                tr.style.animationDelay = `${index * 0.05}s`;
                tr.innerHTML = `
                    <td>${enrollment.studentRegNo}</td>
                    <td>${enrollment.studentName}</td>
                    <td>${enrollment.courseCode}</td>
                    <td>${enrollment.courseTitle}</td>
                    <td>${enrollment.grade}</td>
                `;
                tbody.appendChild(tr);
            });
        } catch (e) {
            console.error('Failed to load enrollments:', e);
        }
    }
};

// Initialize App
document.addEventListener('DOMContentLoaded', () => {
    app.init();
});

// Expose app to global scope for inline onclick handlers
window.app = app;
