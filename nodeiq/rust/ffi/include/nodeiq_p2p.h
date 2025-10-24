#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

int32_t niq_start_node(const char *config_json);

int32_t niq_stop_node(void);

int32_t niq_get_peer_id(char *out_buf, int32_t buf_len);

int32_t niq_advertise(const char *_namespace_json);

int32_t niq_discover(const char *_namespace_str);

int32_t niq_send_query(const char *_to_peer_id, const char *_query_json);

int32_t niq_provider_send_chunk(const char *_query_id, int32_t _seq, const char *_delta_utf8);

int32_t niq_provider_end(const char *_query_id, const char *_status_utf8);

int32_t niq_set_event_callback(void (*callback)(const char*));
