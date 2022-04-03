#ifndef DEFINITIONS_HPP
#define DEFINITIONS_HPP

#include <err.h>

#define CHECK(cmd)      ({ int ret = (cmd); if (ret == -1) { err(1, "%s:%d %s", __FILE__, __LINE__, #cmd);}; ret; })
#define CHECKPTR(cmd)  ({ void *ptr = (cmd); if (ptr == (void *)-1) { err(1, "%s:%d %s", __FILE__, __LINE__, #cmd);}; ptr; })
#define CHECKNULL(cmd) ({ __typeof__(cmd) ptr = (cmd); if (ptr == NULL) { err(1, "%s:%d %s", __FILE__, __LINE__, #cmd);}; ptr; })

#endif // DEFINITIONS_HPP
