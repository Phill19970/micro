#!/bin/bash

# List of services to remove Docker images
services=(
    "user"
    "medical-record"
    "expense"
    "appointment"
    "api-gateway"
    "notification-service"
)

# Function to remove Docker image for a given service
function remove_docker_image {
    service_name=$1

    echo "Removing Docker image for $service_name..."
    docker rmi "capstone-$service_name"
    echo "Docker image for $service_name has been removed!"
}

# Loop through the services and remove Docker images
for service in "${services[@]}"; do
    remove_docker_image "$service"
done

echo "All Docker images have been removed!"
