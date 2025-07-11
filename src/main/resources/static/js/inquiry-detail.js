document.addEventListener('DOMContentLoaded', () => {
    const likeButton = document.getElementById('likeButton');
    const likeCountSpan = document.getElementById('likeCount');

    if (likeButton && likeCountSpan) {
        likeButton.addEventListener('click', async () => {
            const inquiryId = likeButton.dataset.inquiryId;
            if (!inquiryId) {
                console.error('Inquiry ID not found on like button.');
                return;
            }

            try {
                const response = await fetch(`${window.location.origin}/inquiries/${inquiryId}/like`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        // Spring Security CSRF token might be needed for POST requests
                        // If you have CSRF enabled, you'll need to add it here.
                        // For now, assuming CSRF is disabled or handled otherwise.
                    },
                });

                if (response.ok) {
                    const data = await response.json(); // Assuming backend returns updated likes or a success status
                    if (data.liked) { // Assuming backend returns { liked: true, newLikes: X } or similar
                        likeCountSpan.textContent = data.newLikes;
                        alert('좋아요를 눌렀습니다.');
                    } else {
                        alert('이미 좋아요를 누르셨습니다.');
                    }
                } else if (response.status === 401 || response.status === 403) {
                    alert('로그인 후 이용해주세요.');
                    window.location.href = `${window.location.origin}/login`;
                } else {
                    const errorText = await response.text();
                    console.error('Error liking inquiry:', response.status, errorText);
                    alert('좋아요 처리 중 오류가 발생했습니다.');
                }
            } catch (error) {
                console.error('Network error during like request:', error);
                alert('네트워크 오류가 발생했습니다.');
            }
        });
    }
});
