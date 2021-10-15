#include <stdio.h>
#include <string.h>

#define N	200000

int ds[N * 2], cc[N * 2];

int find(int i) {
	return ds[i] < 0 ? i : (ds[i] = find(ds[i]));
}

void join(int i, int j) {
	i = find(i);
	j = find(j);
	if (i == j)
		return;
	if (ds[i] > ds[j])
		ds[i] = j, cc[j] += cc[i];
	else {
		if (ds[i] == ds[j])
			ds[i]--;
		ds[j] = i, cc[i] += cc[j];
	}
}

int main() {
	int t;

	scanf("%d", &t);
	while (t--) {
		int n, m, i, j, cnt;

		scanf("%d%d", &n, &m);
		for (i = 0; i < n * 2; i++)
			ds[i] = -1, cc[i] = i % 2;
		memset(ds, -1, n * 2 * sizeof *ds);
		while (m--) {
			static char s[16];

			scanf("%d%d%s", &i, &j, s), i--, j--;
			if (s[0] == 'c')
				join(i << 1 | 0, j << 1 | 0), join(i << 1 | 1, j << 1 | 1);
			else
				join(i << 1 | 0, j << 1 | 1), join(i << 1 | 1, j << 1 | 0);
		}
		cnt = 0;
		for (i = 0; i < n; i++)
			if (find(i << 1 | 0) == find(i << 1 | 1)) {
				cnt = -1;
				break;
			}
		if (cnt != -1)
			for (i = 0; i < n; i++) {
				int r0 = find(i << 1 | 0), r1 = find(i << 1 | 1);

				if (cc[r1] > cc[r0] || cc[r1] == cc[r0] && r1 > r0)
					cnt++;
			}
		printf("%d\n", cnt);
	}
	return 0;
}
