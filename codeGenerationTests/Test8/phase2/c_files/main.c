#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define N	300000
#define M	300000

int ii[M], jj[M];
int *eh[N], eo[N];

void append(int i, int h) {
	int o = eo[i]++;

	if (o >= 2 && (o & o - 1) == 0)
		eh[i] = (int *) realloc(eh[i], o * 2 * sizeof *eh[i]);
	eh[i][o] = h;
}

int ds[N];
long long aa[N];

int find(int i) {
	return ds[i] < 0 ? i : (ds[i] = find(ds[i]));
}

int join(int i, int j) {
	int tmp;

	i = find(i), j = find(j);
	if (i == j)
		return 0;
	if (eo[i] < eo[j])
		tmp = i, i = j, j = tmp;
	ds[j] = i;
	aa[i] += aa[j];
	while (eo[j])
		append(i, eh[j][--eo[j]]);
	free(eh[j]);
	return 1;
}

int main() {
	static char used[M];
	int n, m, x, h, i, j;
	long long sum;

	scanf("%d%d%d", &n, &m, &x);
	sum = 0;
	for (i = 0; i < n; i++) {
		scanf("%lld", &aa[i]), aa[i] -= x;
		sum += aa[i];
	}
	if (sum + x < 0) {
		printf("NO\n");
		return 0;
	}
	for (i = 0; i < n; i++)
		eh[i] = (int *) malloc(2 * sizeof *eh[i]);
	for (h = 0; h < m; h++) {
		scanf("%d%d", &i, &j), i--, j--;
		ii[h] = i, jj[h] = j;
		append(i, h), append(j, h);
	}
	memset(ds, -1, n * sizeof *ds);
	printf("YES\n");
	for (i = 0; i < n; i++) {
		int r = i;

		while (aa[r = find(r)] > 0 && eo[r]) {
			h = eh[r][--eo[r]];
			used[h] = 1;
			if (join(ii[h], jj[h]))
				printf("%d\n", h + 1);
		}
	}
	for (h = 0; h < m; h++)
		if (!used[h] && join(ii[h], jj[h]))
			printf("%d\n", h + 1);
	return 0;
}
