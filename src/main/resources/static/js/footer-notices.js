document.addEventListener('DOMContentLoaded', async () => {
    const noticeLink = document.getElementById('dynamicNoticeLink');
    let notices = [];
    let currentIndex = 0;

    if (!noticeLink) {
        console.error('dynamicNoticeLink element not found.');
        return;
    }

    try {
        // Fetch notices from the API
        const response = await fetch(`${window.location.origin}/api/notices?size=100&sort=createdAt,desc`); // Fetch a reasonable number of notices
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();

        if (data.content && data.content.length > 0) {
            notices = data.content.map(notice => ({
                title: notice.title,
                id: notice.id
            }));

            // Function to update the notice display
            const updateNotice = () => {
                if (notices.length === 0) {
                    noticeLink.textContent = '공지사항이 없습니다.';
                    noticeLink.href = '#';
                    return;
                }
                const currentNotice = notices[currentIndex];
                noticeLink.textContent = currentNotice.title;
                noticeLink.href = `${window.location.origin}/notices/${currentNotice.id}`;
                currentIndex = (currentIndex + 1) % notices.length;
            };

            // Initial display
            updateNotice();

            // Update every 5 seconds
            setInterval(updateNotice, 5000);
        } else {
            noticeLink.textContent = '공지사항이 없습니다.';
            noticeLink.href = '#';
        }

    } catch (error) {
        console.error('Error fetching notices:', error);
        noticeLink.textContent = '공지사항을 불러오는 데 실패했습니다.';
        noticeLink.href = '#';
    }
});
