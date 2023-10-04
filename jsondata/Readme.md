A quick test to compare memory consumption of Redis String and Redis JSON. 

Used the [airbnb data set](https://gist.github.com/john-guerra/1554ce7ca4bb8248b715d484e1db03f3/raw/cd6ce6a55c5e26dc5f8cdaca5f3adb1ad985ad16/listingsAndReviews.json.zip)


```
docker run -it -e REDIS_ARGS="--maxmemory 50mb" --name redis-stack -p 6379:6379 -p 8001:8001  --rm redis/redis-stack:7.2.0-v0
```

## JSON 
1107 json objects
```
command not allowed when used memory > 'maxmemory'.
{'db0': {'keys': 1107, 'expires': 0, 'avg_ttl': 0}}
{'used_memory': 52441504, 'used_memory_human': '50.01M', 'used_memory_rss': 72482816, 'used_memory_rss_human': '69.12M', 'used_memory_peak': 52457240, 'used_memory_peak_human': '50.03M', 'used_memory_peak_perc': '99.97%', 'used_memory_overhead': 1435432, 'used_memory_startup': 1313296, 'used_memory_dataset': 51006072, 'used_memory_dataset_perc': '99.76%', 'allocator_allocated': 51607056, 'allocator_active': 72451072, 'allocator_resident': 72451072, 'total_system_memory': 25177251840, 'total_system_memory_human': '23.45G', 'used_memory_lua': 31744, 'used_memory_vm_eval': 31744, 'used_memory_lua_human': '31.00K', 'used_memory_scripts_eval': 0, 'number_of_cached_scripts': 0, 'number_of_functions': 0, 'number_of_libraries': 0, 'used_memory_vm_functions': 32768, 'used_memory_vm_total': 64512, 'used_memory_vm_total_human': '63.00K', 'used_memory_functions': 216, 'used_memory_scripts': 216, 'used_memory_scripts_human': '216B', 'maxmemory': 52428800, 'maxmemory_human': '50.00M', 'maxmemory_policy': 'noeviction', 'allocator_frag_ratio': 1.4, 'allocator_frag_bytes': 20844016, 'allocator_rss_ratio': 1.0, 'allocator_rss_bytes': 0, 'rss_overhead_ratio': 1.0, 'rss_overhead_bytes': 31744, 'mem_fragmentation_ratio': 1.4, 'mem_fragmentation_bytes': 20875760, 'mem_not_counted_for_evict': 0, 'mem_replication_backlog': 0, 'mem_total_replication_buffers': 0, 'mem_clients_slaves': 0, 'mem_clients_normal': 61256, 'mem_cluster_links': 0, 'mem_aof_buffer': 0, 'mem_allocator': 'libc', 'active_defrag_running': 0, 'lazyfree_pending_objects': 0, 'lazyfreed_objects': 0}
```

## String

2381 String
```
python3 upload.py
command not allowed when used memory > 'maxmemory'.
{'db0': {'keys': 2381, 'expires': 0, 'avg_ttl': 0}}
{'used_memory': 52421048, 'used_memory_human': '49.99M', 'used_memory_rss': 72482816, 'used_memory_rss_human': '69.12M', 'used_memory_peak': 52457768, 'used_memory_peak_human': '50.03M', 'used_memory_peak_perc': '99.93%', 'used_memory_overhead': 1480080, 'used_memory_startup': 1313296, 'used_memory_dataset': 50940968, 'used_memory_dataset_perc': '99.67%', 'allocator_allocated': 48353696, 'allocator_active': 72451072, 'allocator_resident': 72451072, 'total_system_memory': 25177251840, 'total_system_memory_human': '23.45G', 'used_memory_lua': 31744, 'used_memory_vm_eval': 31744, 'used_memory_lua_human': '31.00K', 'used_memory_scripts_eval': 0, 'number_of_cached_scripts': 0, 'number_of_functions': 0, 'number_of_libraries': 0, 'used_memory_vm_functions': 32768, 'used_memory_vm_total': 64512, 'used_memory_vm_total_human': '63.00K', 'used_memory_functions': 216, 'used_memory_scripts': 216, 'used_memory_scripts_human': '216B', 'maxmemory': 52428800, 'maxmemory_human': '50.00M', 'maxmemory_policy': 'noeviction', 'allocator_frag_ratio': 1.5, 'allocator_frag_bytes': 24097376, 'allocator_rss_ratio': 1.0, 'allocator_rss_bytes': 0, 'rss_overhead_ratio': 1.0, 'rss_overhead_bytes': 31744, 'mem_fragmentation_ratio': 1.5, 'mem_fragmentation_bytes': 24129120, 'mem_not_counted_for_evict': 0, 'mem_replication_backlog': 0, 'mem_total_replication_buffers': 0, 'mem_clients_slaves': 0, 'mem_clients_normal': 38560, 'mem_cluster_links': 0, 'mem_aof_buffer': 0, 'mem_allocator': 'libc', 'active_defrag_running': 0, 'lazyfree_pending_objects': 0, 'lazyfreed_objects': 0}
```