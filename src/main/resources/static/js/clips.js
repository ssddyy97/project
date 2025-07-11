document.addEventListener('DOMContentLoaded', () => {
    const addVideoForm = document.getElementById('addVideoForm');
    const videoListContainer = document.getElementById('videoListContainer');
    const loadMoreBtn = document.getElementById('loadMoreBtn');

    let currentPage = 0;
    let totalPages = 0;

    // Function to extract YouTube video ID from URL
    function getYouTubeVideoId(url) {
        const regExp = /(?:youtube\.com\/(?:[^\/]+\/.+\/|(?:v|e(?:mbed)?)\/|.*[?&]v=)|youtu\.be\/)([^"&?\/\s]{11})/;
        const match = url.match(regExp);
        return (match && match[1]) ? match[1] : null;
    }

    // Function to create a video card HTML
    function createVideoCard(video) {
        const videoId = getYouTubeVideoId(video.videoUrl);
        const embedUrl = videoId ? `https://www.youtube.com/embed/${videoId}?autoplay=1` : video.videoUrl; // Add autoplay for click-to-play
        const thumbnailUrl = video.thumbnailUrl || (videoId ? `https://img.youtube.com/vi/${videoId}/hqdefault.jpg` : '');

        return `
            <div class="col-md-4 mb-4">
                <div class="card h-100">
                    <div class="ratio ratio-16x9 video-thumbnail-container" data-video-id="${videoId}" data-embed-url="${embedUrl}">
                        ${thumbnailUrl ? `<img src="${thumbnailUrl}" class="img-fluid w-100 h-100 object-fit-cover" alt="${video.title} thumbnail">` : ''}
                        <div class="play-button-overlay">
                            <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" fill="currentColor" class="bi bi-play-circle-fill" viewBox="0 0 16 16">
                                <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM6.79 5.093A.5.5 0 0 0 6 5.5v5a.5.5 0 0 0 .79.407l3.5-2.5a.5.5 0 0 0 0-.814l-3.5-2.5z"/>
                            </svg>
                        </div>
                    </div>
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${video.title}</h5>
                        <p class="card-text flex-grow-1">${video.description || ''}</p>
                        <p class="card-text"><small class="text-muted">${new Date(video.createdAt).toLocaleString()}</small></p>
                    </div>
                </div>
            </div>
        `;
    }

    // Function to fetch and display videos
    async function fetchVideos(page) {
        try {
            const response = await fetch(`${window.location.origin}/api/videos?page=${page}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();

            data.content.forEach(video => {
                videoListContainer.insertAdjacentHTML('beforeend', createVideoCard(video));
            });

            currentPage = data.number;
            totalPages = data.totalPages;

            if (currentPage < totalPages - 1) {
                loadMoreBtn.style.display = 'block';
            } else {
                loadMoreBtn.style.display = 'none';
            }
        } catch (error) {
            console.error('Error fetching videos:', error);
            // Optionally display an error message to the user
        }
    }

    // Event listener for clicking on video thumbnails
    videoListContainer.addEventListener('click', (event) => {
        const container = event.target.closest('.video-thumbnail-container');
        if (container && !container.classList.contains('loaded')) {
            const embedUrl = container.dataset.embedUrl;
            const videoId = container.dataset.videoId; // Get videoId to use in title
            if (embedUrl) {
                container.innerHTML = `
                    <iframe src="${embedUrl}" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                `;
                container.classList.add('loaded');
            }
        }
    });

    // Handle form submission
    if (addVideoForm) { // Check if addVideoForm exists (only for ADMIN)
        addVideoForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const video = {
                title: document.getElementById('videoTitle').value,
                description: document.getElementById('videoDescription').value,
                videoUrl: document.getElementById('videoUrl').value,
                thumbnailUrl: document.getElementById('thumbnailUrl').value
            };

            try {
                const response = await fetch(`${window.location.origin}/api/videos`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(video)
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const newVideo = await response.json();
                // Prepend the new video to the list
                videoListContainer.insertAdjacentHTML('afterbegin', createVideoCard(newVideo));
                addVideoForm.reset(); // Clear the form
                // Re-fetch to update pagination if needed, or just adjust totalPages
                if (totalPages === 0 || videoListContainer.children.length === 1) { // First video added
                    currentPage = 0; // Reset page to ensure correct pagination
                    videoListContainer.innerHTML = ''; // Clear existing content
                    fetchVideos(0); // Re-fetch all to get correct pagination state
                } else if (videoListContainer.children.length % 9 === 1 && currentPage === totalPages -1) { // If adding to a full last page
                    totalPages++; // Increment total pages
                    loadMoreBtn.style.display = 'block'; // Show load more button
                }


            } catch (error) {
                console.error('Error adding video:', error);
                alert('영상 추가에 실패했습니다.'); // User-friendly error message
            }
        });
    }

    // Handle load more button click
    loadMoreBtn.addEventListener('click', () => {
        fetchVideos(currentPage + 1);
    });

    // Initial load of videos
    fetchVideos(0);
});